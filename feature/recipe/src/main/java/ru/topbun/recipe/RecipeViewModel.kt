package ru.topbun.recipe

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
import ru.topbun.domain.useCases.favorite.SwitchFavoriteRecipeUseCase
import ru.topbun.domain.useCases.recipe.GetRecipeByIdUseCase

internal class RecipeViewModel(
    recipeId: Int,
    private val getRecipeByIdUseCase: GetRecipeByIdUseCase,
    private val switchFavoriteRecipeUseCase: SwitchFavoriteRecipeUseCase,
    private val snackbarManager: SnackbarManager,
) : MVI<RecipeIntent, RecipeState, RecipeEvent>(RecipeState(recipeId = recipeId)) {

    private var loadJob: Job? = null
    private var favoriteJob: Job? = null
    private var timerJob: Job? = null

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
                        cookingTimerSecondsLeft = if (it.cookingTimerSecondsLeft == 0) {
                            recipe.cookingTime * 60
                        } else it.cookingTimerSecondsLeft,
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
        _state.update {
            it.copy(
                isCookingMode = false,
                cookingTimerSecondsLeft = 0,
                cookingTimerPaused = false,
            )
        }
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
        val text = "Попробуй рецепт «${recipe.title}» в Yumly"
        viewModelScope.launch { _events.send(RecipeEvent.Share(text)) }
    }

    private fun clickAuthor() {
        val authorId = state.value.recipe?.author?.userId ?: return
        viewModelScope.launch { _events.send(RecipeEvent.NavigateToProfile(authorId)) }
    }

    private fun changeIngredientMode(mode: RecipeState.IngredientMode) {
        _state.update { it.copy(ingredientMode = mode) }
    }

    private fun toggleIngredient(index: Int) = _state.update { current ->
        val mode = current.ingredientMode
        val checks = current.ingredientChecks.toMutableMap()
        val set = checks[mode].orEmpty().toMutableSet()
        if (!set.add(index)) set.remove(index)
        checks[mode] = set
        current.copy(ingredientChecks = checks)
    }

    private fun toggleStep(index: Int) = _state.update { current ->
        val set = current.completedSteps.toMutableSet()
        if (!set.add(index)) set.remove(index)
        current.copy(completedSteps = set)
    }

    private fun resetIngredients() = _state.update { current ->
        val mode = current.ingredientMode
        val checks = current.ingredientChecks.toMutableMap()
        checks[mode] = emptySet()
        current.copy(ingredientChecks = checks)
    }

    private fun resetSteps() = _state.update { it.copy(completedSteps = emptySet()) }

    private fun startCooking() {
        val recipe = state.value.recipe ?: return
        val secondsLeft = state.value.cookingTimerSecondsLeft.takeIf { it > 0 }
            ?: (recipe.cookingTime * 60)
        _state.update {
            it.copy(
                isCookingMode = true,
                cookingTimerSecondsLeft = secondsLeft,
                cookingTimerPaused = false,
            )
        }
        startTimerTick()
    }

    private fun stopCooking() {
        timerJob?.cancel()
        _state.update {
            it.copy(
                isCookingMode = false,
                cookingTimerPaused = false,
            )
        }
    }

    private fun pauseTimer() {
        timerJob?.cancel()
        _state.update { it.copy(cookingTimerPaused = true) }
    }

    private fun resumeTimer() {
        if (!state.value.isCookingMode) return
        _state.update { it.copy(cookingTimerPaused = false) }
        startTimerTick()
    }

    private fun resetTimer() {
        timerJob?.cancel()
        val total = state.value.cookingTimerTotalSeconds
        _state.update {
            it.copy(
                cookingTimerSecondsLeft = total,
                cookingTimerPaused = false,
            )
        }
        if (state.value.isCookingMode) startTimerTick()
    }

    private fun startTimerTick() {
        timerJob?.cancel()
        timerJob = viewModelScope.launch {
            while (true) {
                val current = state.value
                if (!current.isCookingMode || current.cookingTimerPaused) return@launch
                if (current.cookingTimerSecondsLeft <= 0) {
                    _events.send(RecipeEvent.CookingDone)
                    _state.update { it.copy(cookingTimerPaused = true) }
                    return@launch
                }
                delay(1000)
                _state.update {
                    it.copy(cookingTimerSecondsLeft = (it.cookingTimerSecondsLeft - 1).coerceAtLeast(0))
                }
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
            RecipeIntent.StartCooking -> startCooking()
            RecipeIntent.StopCooking -> stopCooking()
            RecipeIntent.PauseTimer -> pauseTimer()
            RecipeIntent.ResumeTimer -> resumeTimer()
            RecipeIntent.ResetTimer -> resetTimer()
            is RecipeIntent.ChangeIngredientMode -> changeIngredientMode(intent.mode)
            is RecipeIntent.ToggleIngredient -> toggleIngredient(intent.index)
            is RecipeIntent.ToggleStep -> toggleStep(intent.index)
        }
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
}
