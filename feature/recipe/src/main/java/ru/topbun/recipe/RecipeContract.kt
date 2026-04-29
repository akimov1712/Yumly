package ru.topbun.recipe

import ru.topbun.domain.ScreenUiState
import ru.topbun.domain.entity.recipe.RecipeEntity

internal data class RecipeState(
    val recipeId: Int,
    val fromCache: Boolean,
    val recipe: RecipeEntity? = null,
    val recipeStatus: ScreenUiState = ScreenUiState.Idle,
    val isFavorite: Boolean = false,
    val favoriteLoading: Boolean = false,
    val checkedIngredients: Set<Int> = emptySet(),
    val completedSteps: Set<Int> = emptySet(),
    val timer: TimerState = TimerState(),
    val currentUserId: Int? = null,
    val showDeleteDialog: Boolean = false,
    val deleteLoading: Boolean = false,
) {

    val isOwnRecipe: Boolean
        get() {
            val authorId = recipe?.author?.userId ?: return false
            val userId = currentUserId ?: return false
            return authorId == userId
        }

    val ingredientProgress: Float
        get() {
            val total = recipe?.ingredients?.size ?: 0
            if (total == 0) return 0f
            return checkedIngredients.size.toFloat() / total
        }

    val stepProgress: Float
        get() {
            val total = recipe?.steps?.size ?: 0
            if (total == 0) return 0f
            return completedSteps.size.toFloat() / total
        }

    data class TimerState(
        val mode: TimerMode = TimerMode.Timer,
        val targetSeconds: Int = DEFAULT_TIMER_SECONDS,
        val elapsedSeconds: Int = 0,
        val isRunning: Boolean = false,
    ) {

        val displaySeconds: Int
            get() = when (mode) {
                TimerMode.Timer -> (targetSeconds - elapsedSeconds).coerceAtLeast(0)
                TimerMode.Stopwatch -> elapsedSeconds
            }

        val progress: Float
            get() = when (mode) {
                TimerMode.Timer -> {
                    if (targetSeconds <= 0) 0f
                    else (elapsedSeconds.toFloat() / targetSeconds).coerceIn(0f, 1f)
                }
                TimerMode.Stopwatch -> 0f
            }

        val canStart: Boolean
            get() = when (mode) {
                TimerMode.Timer -> targetSeconds > 0 && elapsedSeconds < targetSeconds
                TimerMode.Stopwatch -> true
            }
    }

    enum class TimerMode(val title: String) {
        Timer("Таймер"), Stopwatch("Секундомер");
    }

    companion object {
        const val DEFAULT_TIMER_SECONDS = 5 * 60
    }

}

internal sealed interface RecipeIntent {

    data object LoadRecipe : RecipeIntent
    data object Refresh : RecipeIntent
    data object ToggleFavorite : RecipeIntent
    data object ClickShare : RecipeIntent
    data object ClickAuthor : RecipeIntent
    data class ToggleIngredient(val index: Int) : RecipeIntent
    data class ToggleStep(val index: Int) : RecipeIntent
    data object ResetIngredients : RecipeIntent
    data object ResetSteps : RecipeIntent

    data class ChangeShowDeleteDialog(val value: Boolean) : RecipeIntent
    data object DeleteRecipe : RecipeIntent

    data class ChangeTimerMode(val mode: RecipeState.TimerMode) : RecipeIntent
    data class ChangeTimerTarget(val seconds: Int) : RecipeIntent
    data object StartTimer : RecipeIntent
    data object PauseTimer : RecipeIntent
    data object ResetTimer : RecipeIntent

}

internal sealed interface RecipeEvent {

    data class NavigateToProfile(val userId: Int) : RecipeEvent
    data class Share(val text: String) : RecipeEvent
    data object RecipeDeleted : RecipeEvent
    data object TimerFinished : RecipeEvent
    data object ShowReview : RecipeEvent

}
