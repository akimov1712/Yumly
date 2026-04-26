package ru.topbun.recipe

import android.content.Context
import android.content.Intent
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.topbun.core.android.MVI
import ru.topbun.core.android.SnackbarManager
import ru.topbun.core.common.error.DataError
import ru.topbun.core.common.onError
import ru.topbun.core.common.onSuccess
import ru.topbun.domain.ScreenUiState
import ru.topbun.domain.entity.recipe.RecipeEntity
import ru.topbun.domain.useCases.account.GetAccountInfoUseCase
import ru.topbun.domain.useCases.favorite.SwitchFavoriteRecipeUseCase
import ru.topbun.domain.useCases.recipe.DeleteRecipeUseCase
import ru.topbun.domain.useCases.recipe.GetRecipeByIdUseCase

internal class RecipeViewModel(
    recipeId: Int,
    private val context: Context,
    private val getRecipeByIdUseCase: GetRecipeByIdUseCase,
    private val switchFavoriteRecipeUseCase: SwitchFavoriteRecipeUseCase,
    private val deleteRecipeUseCase: DeleteRecipeUseCase,
    private val getAccountInfoUseCase: GetAccountInfoUseCase,
    private val snackbarManager: SnackbarManager,
) : MVI<RecipeIntent, RecipeState, RecipeEvent>(RecipeState(recipeId = recipeId)) {

    private var loadJob: Job? = null
    private var favoriteJob: Job? = null
    private var deleteJob: Job? = null
    private var timerJob: Job? = null

    init {
        loadCurrentUser()
    }

    private fun loadCurrentUser() {
        viewModelScope.launch(SupervisorJob()) {
            getAccountInfoUseCase().onSuccess { account ->
                _state.update { it.copy(currentUserId = account.id) }
            }
        }
    }

    private fun loadRecipe() {
        loadJob?.cancel()
        loadJob = viewModelScope.launch(SupervisorJob()) {
            _state.update { it.copy(recipeStatus = ScreenUiState.Loading) }
            getRecipeByIdUseCase(state.value.recipeId).onSuccess { recipe ->
                _state.update {
                    it.copy(
                        recipe = recipe,
                        recipeStatus = ScreenUiState.Success,
                        isFavorite = recipe.isFavorite,
                    )
                }
            }.onError { error, _ ->
                snackbarManager.showMessage(error.toMessage())
                _state.update { it.copy(recipeStatus = ScreenUiState.Error) }
            }
        }
    }

    private fun refresh() {
        timerJob?.cancel()
        _state.update { it.copy(timer = RecipeState.TimerState()) }
        loadRecipe()
    }

    private fun toggleFavorite() {
        val current = state.value
        val recipe = current.recipe ?: return
        if (current.favoriteLoading) return

        val previousFavorite = current.isFavorite
        _state.update { it.copy(isFavorite = !previousFavorite, favoriteLoading = true) }

        favoriteJob?.cancel()
        favoriteJob = viewModelScope.launch(SupervisorJob()) {
            switchFavoriteRecipeUseCase(recipe.id).onSuccess { isFavorite ->
                _state.update { it.copy(isFavorite = isFavorite, favoriteLoading = false) }
            }.onError { error, _ ->
                snackbarManager.showMessage(error.toMessage())
                _state.update { it.copy(isFavorite = previousFavorite, favoriteLoading = false) }
            }
        }
    }

    private fun share() {
        val recipe = state.value.recipe ?: return
        val text = buildShareText(recipe)

        val intent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_TEXT, text)
        }
        context.startActivity(Intent.createChooser(intent, "Поделиться рецептом"))
    }

    private fun clickAuthor() {
        val current = state.value
        val authorId = current.recipe?.author?.userId ?: return
        if (current.isOwnRecipe) {
            snackbarManager.showMessage("Это ваш профиль")
            return
        }
        viewModelScope.launch { _events.send(RecipeEvent.NavigateToProfile(authorId)) }
    }

    private fun changeShowDeleteDialog(value: Boolean) =
        _state.update { it.copy(showDeleteDialog = value) }

    private fun deleteRecipe() {
        val current = state.value
        val recipe = current.recipe ?: return
        if (!current.isOwnRecipe || current.deleteLoading) return

        deleteJob?.cancel()
        deleteJob = viewModelScope.launch(SupervisorJob()) {
            _state.update { it.copy(deleteLoading = true) }
            deleteRecipeUseCase(recipe.id).onSuccess {
                _state.update { it.copy(deleteLoading = false, showDeleteDialog = false) }
                snackbarManager.showMessage("Рецепт удалён")
                _events.send(RecipeEvent.RecipeDeleted)
            }.onError { error, _ ->
                snackbarManager.showMessage(error.toMessage())
                _state.update { it.copy(deleteLoading = false) }
            }
        }
    }

    private fun toggleIngredient(index: Int) = _state.update { current ->
        val set = current.checkedIngredients.toMutableSet()
        if (!set.add(index)) set.remove(index)
        current.copy(checkedIngredients = set)
    }

    private fun toggleStep(index: Int) = _state.update { current ->
        val set = current.completedSteps.toMutableSet()
        if (!set.add(index)) set.remove(index)
        current.copy(completedSteps = set)
    }

    private fun resetIngredients() = _state.update { it.copy(checkedIngredients = emptySet()) }

    private fun resetSteps() = _state.update { it.copy(completedSteps = emptySet()) }

    private fun changeTimerMode(mode: RecipeState.TimerMode) {
        timerJob?.cancel()
        _state.update {
            it.copy(
                timer = it.timer.copy(
                    mode = mode,
                    isRunning = false,
                    elapsedSeconds = 0,
                )
            )
        }
    }

    private fun changeTimerTarget(seconds: Int) {
        if (state.value.timer.isRunning) return
        _state.update {
            it.copy(
                timer = it.timer.copy(
                    targetSeconds = seconds.coerceAtLeast(0),
                    elapsedSeconds = 0,
                )
            )
        }
    }

    private fun startTimer() {
        if (!state.value.timer.canStart) return
        _state.update { it.copy(timer = it.timer.copy(isRunning = true)) }
        startTimerTick()
    }

    private fun pauseTimer() {
        timerJob?.cancel()
        _state.update { it.copy(timer = it.timer.copy(isRunning = false)) }
    }

    private fun resetTimer() {
        timerJob?.cancel()
        _state.update {
            it.copy(
                timer = it.timer.copy(
                    isRunning = false,
                    elapsedSeconds = 0,
                )
            )
        }
    }

    private fun startTimerTick() {
        timerJob?.cancel()
        timerJob = viewModelScope.launch {
            while (true) {
                val timer = state.value.timer
                if (!timer.isRunning) return@launch
                if (timer.mode == RecipeState.TimerMode.Timer && timer.elapsedSeconds >= timer.targetSeconds) {
                    _state.update {
                        it.copy(
                            timer = it.timer.copy(
                                isRunning = false,
                                elapsedSeconds = it.timer.targetSeconds,
                            )
                        )
                    }
                    snackbarManager.showMessage("Готово! Таймер завершён")
                    return@launch
                }
                delay(1000)
                _state.update { it.copy(timer = it.timer.copy(elapsedSeconds = it.timer.elapsedSeconds + 1)) }
            }
        }
    }

    override suspend fun handleIntent(intent: RecipeIntent) {
        when (intent) {
            RecipeIntent.LoadRecipe -> loadRecipe()
            RecipeIntent.Refresh -> refresh()
            RecipeIntent.ToggleFavorite -> toggleFavorite()
            RecipeIntent.ClickShare -> share()
            RecipeIntent.ClickAuthor -> clickAuthor()
            RecipeIntent.ResetIngredients -> resetIngredients()
            RecipeIntent.ResetSteps -> resetSteps()
            RecipeIntent.DeleteRecipe -> deleteRecipe()
            RecipeIntent.StartTimer -> startTimer()
            RecipeIntent.PauseTimer -> pauseTimer()
            RecipeIntent.ResetTimer -> resetTimer()
            is RecipeIntent.ChangeShowDeleteDialog -> changeShowDeleteDialog(intent.value)
            is RecipeIntent.ChangeTimerMode -> changeTimerMode(intent.mode)
            is RecipeIntent.ChangeTimerTarget -> changeTimerTarget(intent.seconds)
            is RecipeIntent.ToggleIngredient -> toggleIngredient(intent.index)
            is RecipeIntent.ToggleStep -> toggleStep(intent.index)
        }
    }

    private fun buildShareText(recipe: RecipeEntity): String = buildString {
        appendLine(recipe.title)
        appendLine()
        if (recipe.ingredients.isNotEmpty()) {
            appendLine("Ингредиенты:")
            recipe.ingredients.forEach { ingredient ->
                appendLine("• ${ingredient.name} — ${ingredient.value}")
            }
            appendLine()
        }
        if (recipe.steps.isNotEmpty()) {
            appendLine("Приготовление:")
            recipe.steps.forEachIndexed { index, step ->
                appendLine("${index + 1}. ${step.description}")
                appendLine()
            }
        }
        appendLine("———")
        appendLine("Рецепт взят из мобильного приложения Yumly")
        append("Скачать: $RUSTORE_URL")
    }

    private fun DataError.toMessage(): String = when (this) {
        DataError.Network.UNAUTHORIZED -> "Пользователь не авторизован"
        DataError.Network.BAD_REQUEST -> "Ошибка при загрузке рецепта"
        DataError.Network.INVALID_DATA -> "Проверьте корректность данных"
        DataError.Network.NOT_FOUND -> "Рецепт не найден"
        DataError.Network.FORBIDDEN -> "Нет доступа к рецепту"
        DataError.Network.REQUEST_TIMEOUT -> "Время ожидания превышено. Проверьте интернет-соединение или попробуйте позже"
        DataError.Network.SERIALIZATION -> "При получении данных произошла ошибка"
        DataError.Network.SERVER_ERROR -> "Произошла ошибка на сервере. Попробуйте позже"
        DataError.Network.NO_INTERNET -> "Отсутствует интернет-соединение"
        else -> "Произошла ошибка. Попробуйте позже"
    }

    companion object {
        private const val RUSTORE_URL = "https://www.rustore.ru/catalog/app/ru.topbun.yumly"
    }
}
