# Yumly — правила и архитектура проекта

> Файл-шпаргалка для меня (Claude). Кратко, но исчерпывающе фиксирую соглашения, чтобы новый код выглядел и вёл себя как остальной проект.

---

## 1. Общая архитектура

Проект — Android-приложение на Kotlin + Jetpack Compose, разбитое на множество Gradle-модулей по слоям и фичам. Используется Clean Architecture: `domain → data → feature/* → app`.

```
app/                       — composition root (RootScreen, MainActivity, DI-сборка, навигация)
core/
  common/                  — чистый Kotlin: Result, Error, Validator, HttpStatusCode, Email
  android/                 — Android-специфика: MVI базовый класс, SnackbarManager, Internet
  ui/                      — общие Compose-компоненты, Colors, Typography, Fonts, утилиты
domain/                    — entity, repository (interfaces), useCases, validation, ScreenUiState
data/                      — repository Impl, Retrofit api, DTO + toEntity(), Room (Dao/Dbo), DataStore, DI
navigation/                — провайдеры экранов через Voyager ScreenRegistry (обвязка для cross-feature навигации)
feature/<name>/            — отдельный модуль на каждый экран:
  <Name>Screen.kt          — Composable экран (Tab или Screen из voyager)
  <Name>Contract.kt        — State + Intent + Event
  <Name>ViewModel.kt       — наследник core.android.MVI
  components/              — приватные UI-компоненты этого экрана
  di/<Name>Module.kt       — Koin-модуль фичи
  fragments/               — (опционально) подэкраны/шаги внутри одной фичи (см. upload)
```

Зависимости между модулями: `feature → core:ui, core:android, domain, navigation`. Бизнес-данные (`data`) собираются только в `app` через DI — feature-модули о data не знают, они работают через `domain` (UseCase / Repository интерфейс).

Package root для всех модулей — `ru.topbun.<имя_модуля>` (например, `ru.topbun.home`, `ru.topbun.upload`, `ru.topbun.core.ui`).

---

## 2. Стек и библиотеки

Версии лежат в `gradle/libs.versions.toml`. Используем `libs.*` алиасы в каждом `build.gradle.kts`. Свои версии не вписываем рядом — только через каталог.

- **Kotlin 2.3.x**, JVM target 17, `compileSdk = 36`, `minSdk = 26`.
- **Jetpack Compose** (BOM 2025.10.01) + Material3.
- **Voyager** 1.1.0-beta03 — навигация (`Navigator`, `Tab`, `ScreenRegistry`, `ScaleTransition`, `voyager-koin`).
- **Koin** 4.x (BOM) — DI (`koinViewModel()`, `koinInject()`, `viewModelOf(::Foo)`, `viewModel { (params) -> ... }`).
- **Retrofit 3** + Gson + OkHttp logging — сеть.
- **Room 2.8** (`ksp`-плагин) — локальная БД.
- **DataStore Preferences** + `security-crypto` — конфиги/токены.
- **Coil 3** (`coil-compose`, `coil-network-okhttp`) — загрузка картинок.
- **compose-shimmer** — shimmer placeholder'ы.
- **sh.calvin.reorderable** — drag&drop списки (используется в upload).
- **accompanist-systemuicontroller** — статусбар.
- **datepicker / wheelview** от ozcanalasalvar — `AppTimePicker`.

Чистые модули используют JVM plugin (`jetbrains-kotlin-jvm`); Compose-фичи — `android-library` + `kotlin-android` + `kotlin-compose`.

---

## 3. Слой domain

- **Entity** — `data class`, никаких аннотаций, лежат в `domain/entity/<group>/`. Ничего из Android/Compose сюда не тянем.
- **Repository** — `interface` в `domain/repository/<group>/`. Все методы возвращают `Result<D, DataError>` (см. ниже).
- **UseCase** — отдельный класс на действие, имя оканчивается на `UseCase`. Делает один `operator fun invoke(...)` (suspend, если нужно), просто проксирует вызов в репозиторий. Пример:
  ```kotlin
  class GetRecipeUseCase(private val repository: RecipeRepository) {
      suspend operator fun invoke(data: GetRecipeEntity) = repository.getRecipe(data)
  }
  ```
- **Validator** — `domain/validation/<group>/<X>Validator.kt` реализует `core.common.Validator<T>`, возвращает `Result<Unit, <X>ValidatorError>`. Ошибки — `enum class` в том же файле или рядом.
- **ScreenUiState** (`domain/ScreenUiState.kt`) — глобальный enum `Idle / Loading / Success / Error` с computed-свойствами `isLoading`, `isError`, `isSuccess`. Используется во всех State'ах для статусов запросов.

### Result / Error

`core.common.Result<D, E : RootError>` — `sealed class` с `Success(data)` / `Error(error, data?)`. Используются inline-расширения:
```kotlin
result.onSuccess { ... }.onError { error, _ -> ... }
```
`DataError.Network` — `enum` со всеми типовыми кодами (`UNAUTHORIZED`, `INVALID_DATA`, `BAD_REQUEST`, `NOT_FOUND`, `FORBIDDEN`, `NOT_VERIFIED`, `EXISTS`, `REQUEST_TIMEOUT`, `SERIALIZATION`, `SERVER_ERROR`, `NO_INTERNET`, `UNKNOWN`).

---

## 4. Слой data

- Реализация репозиториев: `data/repository/<group>/<X>RepositoryImpl.kt`, всегда `internal`. Получают `Context`, Retrofit `Api`, DAO, DataStore.
- Каждое обращение к сети заворачивается в `context.exceptionWrapper { ... }` — оно мапит исключения в `Result.Error(DataError.Network.*)`.
- Внутри: проверка `response.isSuccessful && body != null` → `Result.Success`, иначе `when(response.code())` маппит `HttpStatusCode.*` на `DataError.Network.*`.
- API: `data/source/remote/api/<group>/<X>Api.kt`, `internal interface` с Retrofit-аннотациями (`@POST`, `@Body`, `@Path`).
- DTO: `data/source/remote/dto/<group>/<X>Dto.kt` (или `<X>Request.kt` / `<X>Response.kt`). Каждый DTO `internal` и имеет `fun toEntity()` (а также top-level `internal fun List<XDto>.toEntity()`). Маппинг — только в data, в domain DTO не утекает.
- Room: `data/source/local/database/<group>/<X>Dao.kt` + `dbo/<X>Dbo.kt`. БД собирается в `AppDatabase`.
- DataStore: `data/source/local/config/DataStoreManager.kt` + `Config.kt`.

---

## 5. Презентационный слой — главный паттерн (MVI)

### 5.1 Базовый класс

`core.android.MVI<I, S, E>(state: S) : ViewModel()` (см. `core/android/.../MVI.kt`):

```kotlin
abstract class MVI<I, S, E>(state: S) : ViewModel() {
    protected val _intent = Channel<I>(Channel.UNLIMITED)
    protected val _state  = MutableStateFlow(state)
    val state = _state.asStateFlow()
    protected val _events = Channel<E>()
    val events = _events.receiveAsFlow()

    fun sendIntent(intent: I) = viewModelScope.launch { _intent.send(intent) }
    protected abstract suspend fun handleIntent(intent: I)

    init { observeIntent() }
}
```

Каждый экранный VM наследует от него и реализует `handleIntent` через `when(intent)`.

### 5.2 Contract — `<Name>Contract.kt`

В одном файле живут `<Name>State` (data class), `<Name>Intent` (sealed interface), `<Name>Event` (sealed interface). Всё `internal`.

**State** — иммутабельный `data class` со всеми полями экрана:

- Текстовые поля, флаги диалогов (`showDialogXxx: Boolean`), индексы выбранных вкладок/типов, фильтры — лежат прямо в State.
- Списки оборачиваются во вложенный `data class XListUiState(val items, val status: ScreenUiState, val isEndList: Boolean)`. Это стандарт для пагинации (см. `HomeState.RecipeListUiState`, `AssistantState.ChatListUiState`, `ProfileState.RecipeListUiState`).
- Состояние экрана целиком (когда нужны разные ветки UI) описывается отдельным enum, чаще всего `XxxUiState { SUCCESS, NEED_AUTH }`, или sealed-иерархией (`Mode.Self` / `Mode.Other`, `PublishRecipeUiState.None` / `Success(id)`).
- Внутри State держим **только данные**; бизнес-вычисления выносятся в `val xxx: T get() = ...` (computed properties): `isFilterChanged`, `searchTypeVisible`, `nextButtonEnabled`, `canSendMessage`, `visibleMessages`, `targetUserId`, `visibleList`, `totalCalories` и т.п. Это позволяет UI писать в стиле `if (state.canSendMessage)` без магии в композаблах.
- Енумы данных экрана (`SearchType`, `ProfileTab`, `NutrientsEnum`, `UploadFragments`) кладутся внутрь State либо рядом в Contract.
- Списки `LazyListState` для Compose-листов тоже кладутся в State (`recipeListState: LazyListState = LazyListState()`), чтобы переживать между recompose без потери позиции.

**Intent** — `sealed interface XxxIntent` с `data object` и `data class` ветками. Имена идут в одном из стилей:
- `Change<Field>(val value: ...)` — для обновления одного поля (`ChangeSearch`, `ChangeMessageText`, `ChangeShowDialogClearData`).
- `Click<Action>` / `<Action>` — для действий (`ClickLogin`, `LoadRecipe`, `RefreshRecipe`, `PublishRecipe`, `SendMessage`, `Logout`).
- `Add<Item>` / `Remove<Item>(index)` / `Reorder<Item>(fromIndex, toIndex)` — для CRUD над списками внутри экрана.

**Event** — `sealed interface XxxEvent` для одноразовых эффектов: навигация, тосты-команды, `LoggedOut`. Если событий нет — оставляем пустой sealed interface (`internal sealed interface HomeEvent {}`). Snackbar **не** проходит через Event — это глобальный канал (см. ниже).

### 5.3 ViewModel — `<Name>ViewModel.kt`

```kotlin
internal class HomeViewModel(
    private val getRecipeUseCase: GetRecipeUseCase,
    private val snackbarManager: SnackbarManager,
) : MVI<HomeIntent, HomeState, HomeEvent>(HomeState()) {
    private var recipeLoadJob: Job? = null

    private fun changeSearch(value: String) { _state.update { it.copy(search = value) } }

    private fun loadRecipes() = with(state.value) {
        recipeLoadJob?.cancel()
        recipeLoadJob = viewModelScope.launch(SupervisorJob()) {
            _state.update { it.copy(recipeList = recipeList.copy(status = ScreenUiState.Loading)) }
            getRecipeUseCase(GetRecipeEntity(...)).onSuccess { items ->
                _state.update { it.copy(recipeList = recipeList.copy(
                    recipes = it.recipeList.recipes + items,
                    status = ScreenUiState.Success,
                    isEndList = items.isEmpty(),
                )) }
            }.onError { error, _ ->
                snackbarManager.showMessage(error.toMessage())
                _state.update { it.copy(recipeList = recipeList.copy(status = ScreenUiState.Error)) }
            }
        }
    }

    override suspend fun handleIntent(intent: HomeIntent) {
        when (intent) {
            is HomeIntent.ChangeSearch -> changeSearch(intent.value)
            HomeIntent.LoadRecipe -> loadRecipes()
            // ...
        }
    }

    private fun DataError.toMessage(): String = when (this) { ... }
}
```

Правила:
- Класс `internal`.
- Зависимости — конструктором, инжектятся Koin'ом (`viewModelOf(::HomeViewModel)` / `viewModel { (mode) -> ... }` для параметризованных).
- Все приватные методы — однострочные `_state.update { it.copy(...) }` или launch'и с тем же паттерном.
- Хранимые `Job?` для отменяемых операций (`recipeLoadJob`, `selectedChatLoadJob`, `sendMessageJob`, `publishRecipeJob`, `profileJob`, `recipesJob`, `likedJob`, `followJob`). Перед запуском — `job?.cancel()`.
- Сетевые вызовы запускаются в `viewModelScope.launch(SupervisorJob()) { ... }`, чтобы падение одной корутины не валило остальное.
- Перед запросом — `_state.update { ...status = Loading }`. После — `Success` / `Error`. Ошибка показывается через `snackbarManager.showMessage(error.toMessage())`.
- В каждом VM, где есть сеть, объявляется приватная extension `private fun DataError.toMessage(): String = when(this) { ... }` с человеко-читаемыми сообщениями на **русском** (это пользовательские строки; код, идентификаторы, внутренние названия — английский).
- Реактивные комбинации полей делаем через `combine(state.map {...}.distinctUntilChanged(), ...).debounce(500).collect { refresh() }` в `init { observeXxx() }` (см. `HomeViewModel.observeRecipeChanges`).
- Оптимистичные обновления держим отдельным полем (`optimisticMessages`, `optimistic` в `switchFollow`), на ошибку откатываем.
- Пагинация: проверяем `if (list.status.isLoading || list.isEndList) return`, грузим со `offset = list.items.size`, на пустой ответ ставим `isEndList = true`. Дедуп — `.distinctBy { it.id }`.
- Для пользовательского ввода применяем верхние границы прямо в setter'е (`if (value.length <= 72) ...`, `<= MAX_MESSAGE_LENGTH`).
- Внутри ветвей `with(state.value) { ... }` или `with(_state.value) { ... }` — стандартный приём, чтобы не таскать `state.value.foo` много раз.
- `_events.send(...)` — для разовых эффектов, всегда в корутине (либо `viewModelScope.launch { _events.send(...) }`, либо изнутри уже запущенного `launch`).

### 5.4 Screen — `<Name>Screen.kt`

- Объявляется как `object XxxScreen : Screen` (обычный экран Voyager) или `object XxxScreen : Tab` (для табов в Dashboard'е). У `Tab` — `override val options @Composable get() = TabOptions(index, title, icon = painterResource(...))`.
- Внутри `@Composable override fun Content()`:
  1. `val viewModel: XxxViewModel = koinViewModel()` (или с `parametersOf(...)`).
  2. `val state by viewModel.state.collectAsState()`.
  3. `val navigator = LocalNavigator.currentOrThrow.parent` (для табов берём `parent`, чтобы пушить поверх дашборда).
  4. `LaunchedEffect(Unit) { viewModel.sendIntent(XxxIntent.CheckSession) }` — типичная инициализация.
  5. `ObserveAsEvents(viewModel.events) { event -> when(event) { ... } }` — подписка на одноразовые события (только если есть Event'ы).
  6. Корневой контейнер с `.fillMaxSize().background(Colors.BACKGROUND).statusBarsPadding()` (или `systemBarsPadding()`).
  7. Если экран имеет несколько глобальных режимов (`SUCCESS / NEED_AUTH`) — `when(state.uiState) { SUCCESS -> XxxContent(); NEED_AUTH -> UnauthorizedSection { navigator.push(...) }; else -> {} }`.
  8. Диалоги монтируются после контента: `if (state.showDialogXxx) { XxxDialog(onDismissRequest = { sendIntent(ChangeShowDialogXxx(false)) }, ...) }`.
- Навигация по фичам: только через `ScreenRegistry.get(<XxxScreenProvider>.<Action>)` из модуля `:navigation`. Внутри фичи **никогда** не импортируем `Screen`-объекты других фич.
- В Screen'ах не должно быть ничего, кроме маппинга `state` → composable + отправки `Intent`'ов и обработки `Event`'ов. Никаких бизнес-вычислений.
- Если экран длинный, основной body выносим в `@Composable private fun XxxContent(viewModel: XxxViewModel = koinViewModel())` рядом, в этом же файле (см. `UploadScreen` / `AssistantScreen`).

### 5.5 components/ — структура UI

Это *главное* правило стиля: **каждый экран собирается из множества маленьких приватных композаблов в подпапке `components/`**.

- Composable'ы либо `internal fun XxxSection(...)`, либо `internal fun XxxHeader(...)` — приватные на модуль.
- Один файл на компонент. Имя файла = имя функции. Если файл состоит из 2-3 связанных мелких функций (`Avatar`, `Stats`, `StatDivider` для `ProfileInfo`) — они там же, но `private`.
- Параметры компонента — данные + лямбды-callback'и. Никаких ViewModel внутрь — VM прокидываем только в крупные `Fragment`/`Content`-композаблы (`BasicFragment`, `ContentFragment`, `ProfileContent`).
- Обёртка контента в "карточку" — через единый `SectionWrapper` (см. `feature/upload/.../components/SectionWrapper.kt`):
  ```kotlin
  @Composable
  internal fun FoodNameSection(name: String, onChangeName: (String) -> Unit) =
      SectionWrapper("Название рецепта") {
          AppOutlinedTextField(...)
      }
  ```
  Шаблон: `clip(RoundedCornerShape(28.dp)).background(Colors.WHITE).padding(top = 20.dp, bottom = 24.dp)` + заголовок `Typography.H2` сверху.
- Заголовок секции с количеством — через `buildAnnotatedString { append("Ингредиенты "); withStyle(SpanStyle(color = Colors.SECONDARY_TEXT)) { append("(${size})") } }`.
- Для drag&drop списков — `ReorderableColumn` из `sh.calvin.reorderable`, `key(item)`, `ReorderableItem`, `Modifier.draggableHandle()`. Тень показывается через `dropShadow(shape, Shadow(radius, color, alpha = if (isDragging) 1f else 0f))`.

### 5.6 fragments/ (только сложные экраны)

Когда экран — это многошаговый флоу (как Upload), внутри него лежат `fragments/` с enum'ом шагов и Composable'ом на каждый шаг:

```kotlin
internal enum class UploadFragments { Basic, Content }
```

`UploadScreen` свитчит `when(state.selectedFragment) { Basic -> BasicFragment(); Content -> ContentFragment() }`. Каждый фрагмент — `Column(verticalArrangement = Arrangement.spacedBy(20.dp))` из `XxxSection`'ов.

### 5.7 di/ модуль фичи

```kotlin
val homeModule = module {
    viewModelOf(::HomeViewModel)
}
```
или с параметрами:
```kotlin
val profileModule = module {
    viewModel { (mode: ProfileState.Mode) ->
        ProfileViewModel(mode, get(), get(), ...)
    }
}
```
Все feature-модули собираются в `app/di/FeatureModule.kt` через `includes(...)`.

---

## 6. UI / стиль / темы

- Корневой пакет тем: `ru.topbun.core.ui.theme`. **Material Theme не используется напрямую**, цвета берутся из синглтона `Colors`, типографика — из `Typography`, шрифты — `Fonts.INTER`.
- `Colors`: `PRIMARY`, `SECONDARY`, `MAIN_TEXT`, `BLUE_TEXT`, `SECONDARY_TEXT`, `OUTLINE`, `ERROR`, `FORM`, `BACKGROUND`, `SHIMMER`, `WHITE`, `BLACK`, `GREEN`, `ORANGE`, `RED`. Все — `Color(0xff...)`.
- `Typography`: `H1` (Bold 22sp), `H2` (Bold 17sp), `H3` (Bold 15sp), `P1` (Medium 17sp), `P2` (Medium 15sp), `S` (Medium 12sp), `Placeholder`.
- Скругления: `RoundedCornerShape(44.dp)` — крупные карточки/кнопки/поиск; `RoundedCornerShape(32.dp)` — основные кнопки/инпуты; `RoundedCornerShape(28.dp)` — секции; `RoundedCornerShape(16.dp)` — мелкие плитки. Кружочки — `CircleShape`.
- Padding'и стандартные: внешний горизонтальный экрана — `12.dp`; внутри карточки — `24.dp` горизонтально, `20.dp` сверху / `24.dp` снизу.
- Spacers: используем `Height(20.dp)`, `Width(20.dp)`, `Weight(1f)` из `core.ui.components.Spacers` — никаких `Spacer(Modifier.height(...))` руками.
- Тень — `Modifier.dropShadow(shape, Shadow(radius = 4.dp, alpha = 0.1f))`.
- Для статусбара — `enableEdgeToEdge()` в `MainActivity` + `.systemBarsPadding()`/`.statusBarsPadding()` в корне экрана.
- Иконки — `painterResource(R.drawable.ic_xxx)` из `core.ui` (общая R) или из локальной R фичи. Tint обычно `Colors.MAIN_TEXT` / `Colors.BLUE_TEXT` / `Colors.PRIMARY`.
- Загрузка картинок — `AppAsyncImage(url = ..., contentScale = ...)` (обёртка над Coil).
- Ripple/click — `Modifier.rippleClickable()` из `core.ui.utils`.
- Bottom-bar отступ — через `LocalBottomBarPadding` / `useBottomBarPadding()` (нужно для содержимого, которое не должно прятаться под таб-баром).
- Базовые компоненты из `core/ui`: `AppButton`, `AppOutlinedButton`, `AppTextButton`, `AppTextField`, `AppOutlinedTextField`, `AppSlider`, `AppRangeSlider`, `AppPullRefresh`, `AppTimePicker`, `AppAsyncImage`, `AppSnackbarHost`, `OtpInput`, `RecipeList`, `RecipeItem`, `RecipeShimmer`, `PaginationList`, `DialogWrapper`, `BottomDialogWrapper`, `UnauthorizedSection`, `DifficultyItem`, `PulseLoading`. Не пишем свои аналоги — переиспользуем.
- Для пагинированных списков **берём `PaginationList<T>`** или `RecipeList`: они сами триггерят `onLoadMore` при приближении к концу, рисуют шиммер / footer / "Загрузить снова".

---

## 7. Snackbar и события

- `SnackbarManager` — синглтон в Koin (`single { SnackbarManager() }`, см. `snackbarModule`). Используется во всех ViewModel'ах для показа ошибок.
- В `RootScreen` слушается через `ObserveAsEvents(snackbarManager.messages) { snackbarHostState.showSnackbar(it) }`, рисуется `AppSnackbarHost`.
- Тост (`Toast.makeText(...)`) допустим в Screen для разовых подтверждений (в Upload — "Данные успешно очищены"), но новые сообщения предпочтительнее лить в `SnackbarManager`.
- `ObserveAsEvents` — кастомный composable (см. `core/ui/utils/ObserveAsEvents.kt`), он использует `repeatOnLifecycle(STARTED)` и `Dispatchers.Main.immediate`. Для подписки на VM events — только он, не `LaunchedEffect` напрямую.

---

## 8. Навигация

- `Voyager`. В `RootScreen` — `Navigator(SplashScreen) { ScaleTransition(it) }`.
- Между фичами — через `:navigation`-модуль и `ScreenRegistry`. Каждый feature предоставляет свой `XxxScreenProvider` (например, `AuthScreenProvider.Login`, `ProfileScreenProvider.Settings`, `RootScreenProvider.Dashboard`).
- В Screen берём `LocalNavigator.currentOrThrow` (или `.parent` для табов), и пушим/реплейсим: `navigator.push(screen)`, `navigator.replaceAll(screen)`, `navigator.root().replaceAll(screen)` (через `ru.topbun.navigation.utills.root`).
- Навигация инициируется только в Screen, никогда не из ViewModel напрямую. ViewModel шлёт `Event.NavigateToXxx`, Screen его обрабатывает.

---

## 9. Особенности экранов (быстрый референс)

### Home (`feature/home`)
- Tab №0. Состав: `Header` (поле поиска + кнопка фильтра с точкой `isFilterChanged`), `SearchTypeBar` (chip-toggle "All / Subscribers", скрывается при скролле через `searchTypeVisible`), `RecipeList`, `HomeFilterDialog`.
- Фильтры/поиск/тип триггерят `combine(...).debounce(500)` в `init`, который рефрешит список.
- `RecipeListUiState` — стандарт пагинации.

### Upload (`feature/upload`)
- Tab №1. Двухшаговый флоу через `UploadFragments(Basic, Content)`. `Header` показывает либо "Очистить" + счётчик `selectedOrder/N`, либо "Назад" + "Опубликовать".
- `BasicFragment` = `Column(spacedBy(20.dp))` из секций: `PreviewPicker`, `FoodNameSection`, `DescriptionSection`, `DurationSection`, `NutrientsSection`, `DifficultySection` + кнопка "Далее".
- `ContentFragment` = `IngredientsSection` + `StepsSection` (drag&drop через `ReorderableColumn`) + диалоги `AddIngredientDialog`, `AddStepDialog`.
- Сохранение — через `AddRecipeValidator` (доменный валидатор), потом `AddRecipeUseCase`. Картинки заранее загружаются `UploadFileUseCase` → URL.
- При `NEED_AUTH` — `UnauthorizedSection { navigator.push(Auth.Login) }`.

### Assistant (`feature/assistant`)
- Tab №2. Чат с GPT.
- Слои Box'ом: фон с `MessageList` (центрируется при загрузке/ошибке), оверлей `Header` сверху, `AssistantInputBar` снизу. Высоты top/bottom меряем через `onGloballyPositioned` и складываем в `chatContentPadding`.
- При пустом чате — `AssistantPlaceholder { showHistoryDialog }`. При лимите — `MessageLimitBlock`.
- Оптимистичные сообщения: при `SendMessage` сразу добавляем `optimisticMessages = listOf(user, assistant-empty)`, после успеха — заменяем целым `selectedChat` с сервера.
- `LaunchedEffect(visibleMessages.size, ...)` скроллит к последнему сообщению.

### Profile (`feature/profile`)
- Tab №4 (есть и `Self`-режим как таб, и `Other(userId)` — пушим на полный экран через `ProfileScreenProvider`).
- `ProfileViewModel` принимает `mode: ProfileState.Mode` через `parametersOf(...)`.
- Структура: `ProfileHeader` (back / title / settings), `ProfileShimmer` пока загрузка, дальше `ProfileRecipeList` с шапкой `ProfileInfo` (Avatar + username + email + Stats + Follow-кнопка) и `ProfileTabsBar` (`MyRecipes / Liked`).
- `visibleList` / `visibleListState` в State выбирают активную вкладку.
- Подписка/отписка — оптимистична, на ошибку откат к предыдущему `profile`.
- Logout: `logoutUseCase()` → `ProfileEvent.LoggedOut` → `navigator.root().replaceAll(authScreen)`.

---

## 10. Кодстайл и мелочи

- Видимость по умолчанию — `internal` для всего, что не ушло в публичный API модуля. `XxxState`, `XxxIntent`, `XxxEvent`, `XxxViewModel`, секции/компоненты — `internal`. `XxxScreen`-object и Koin-`module` (`val homeModule`) — `public`.
- Имена: `XxxState`, `XxxIntent`, `XxxEvent`, `XxxViewModel`, `XxxScreen`, `XxxUseCase`, `XxxRepository(Impl)`, `XxxApi`, `XxxDto`, `XxxDbo`, `XxxEntity`, `XxxValidator`, `XxxValidatorError`. UI-компоненты — `XxxSection`, `XxxHeader`, `XxxDialog`, `XxxButton` и т.д.
- Никаких `MutableState` или `remember { mutableStateOf(...) }` для бизнес-данных — всё в State VM'a. Локальный `remember` оправдан только для чисто-UI-измерений (`topBarHeight`, `bottomBarHeight`, `LazyListState` если он не должен переживать пересоздание VM).
- `Modifier` — параметр первый после данных, по дефолту `Modifier`. В нашем коде его часто не объявляют у внутренних компонент — это ок, но **публичные** компоненты в `core/ui` обязательно принимают `modifier: Modifier = Modifier`.
- Текст пользователю — всегда строкой в коде на русском (пока без `R.string`). Технические идентификаторы — английский.
- Лямбды-callback'ы: trailing-lambda для основного действия (`onClick` обычно последний), либо именованные параметры если несколько.
- `@Composable` функции — без префикса `fun View...`. Имена существительными или фразами: `Header`, `SearchTypeBar`, `IngredientsSection`, `ProfileInfo`. Только `internal`/`private`. PascalCase.
- Не использовать `LiveData`. Только `StateFlow` / `Channel` / `Flow`.

---

## 11. Чек-лист, когда добавляю новый экран `feature/<name>`

1. Создать модуль `feature/<name>/build.gradle.kts` (плагины: `android-library`, `kotlin-android`, `kotlin-compose`; зависимости — `core:ui`, `core:android`, `domain`, `navigation`, нужные другие фичи; Voyager + Koin).
2. Добавить `include(":feature:<name>")` в `settings.gradle.kts`.
3. Файлы:
   - `<Name>Contract.kt` — State/Intent/Event (`internal`).
   - `<Name>ViewModel.kt` — `internal class : MVI<I,S,E>(...)`.
   - `<Name>Screen.kt` — `object <Name>Screen : Screen` (или `: Tab`). Внутри `Content()` — koinViewModel, collectAsState, ObserveAsEvents (если есть Event'ы), LaunchedEffect инициализации, корневой Box/Column с системными отступами.
   - `components/` — мелкие composable'ы, через `SectionWrapper` если карточка.
   - `di/<Name>Module.kt` — `viewModelOf(::<Name>ViewModel)`.
4. Подключить `<name>Module` в `app/di/FeatureModule.kt`.
5. Если нужна навигация снаружи — добавить `XxxScreenProvider` в `navigation` и `ScreenRegistry.register { ... }` где собирается реестр.
6. Использовать только `Colors`, `Typography`, `Fonts`, готовые `App*`-компоненты. Snackbar — через `SnackbarManager` (DI). Ошибки маппить через локальный `DataError.toMessage()`.
