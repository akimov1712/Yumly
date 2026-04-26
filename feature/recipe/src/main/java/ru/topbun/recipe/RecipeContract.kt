package ru.topbun.recipe

import ru.topbun.domain.ScreenUiState
import ru.topbun.domain.entity.recipe.RecipeEntity

internal data class RecipeState(
    val recipeId: Int,
    val recipe: RecipeEntity? = null,
    val recipeStatus: ScreenUiState = ScreenUiState.Idle,
    val isFavorite: Boolean = false,
    val favoriteLoading: Boolean = false,
    val ingredientMode: IngredientMode = IngredientMode.Stock,
    val ingredientChecks: Map<IngredientMode, Set<Int>> = IngredientMode.entries.associateWith { emptySet() },
    val completedSteps: Set<Int> = emptySet(),
    val isCookingMode: Boolean = false,
    val cookingTimerSecondsLeft: Int = 0,
    val cookingTimerPaused: Boolean = false,
) {

    val checkedForCurrentMode: Set<Int>
        get() = ingredientChecks[ingredientMode].orEmpty()

    val ingredientProgress: Float
        get() {
            val total = recipe?.ingredients?.size ?: 0
            if (total == 0) return 0f
            return checkedForCurrentMode.size.toFloat() / total
        }

    val stepProgress: Float
        get() {
            val total = recipe?.steps?.size ?: 0
            if (total == 0) return 0f
            return completedSteps.size.toFloat() / total
        }

    val cookingTimerTotalSeconds: Int
        get() = (recipe?.cookingTime ?: 0) * 60

    val cookingTimerProgress: Float
        get() {
            val total = cookingTimerTotalSeconds
            if (total == 0) return 0f
            val passed = total - cookingTimerSecondsLeft
            return (passed.toFloat() / total).coerceIn(0f, 1f)
        }

    enum class IngredientMode(val title: String) {
        Stock("У меня есть"),
        Shopping("Список покупок"),
        Cooking("Готовка");
    }

}

internal sealed interface RecipeIntent {

    data object LoadRecipe : RecipeIntent
    data object Refresh : RecipeIntent
    data object ToggleFavorite : RecipeIntent
    data object ClickShare : RecipeIntent
    data object ClickAuthor : RecipeIntent
    data class ChangeIngredientMode(val mode: RecipeState.IngredientMode) : RecipeIntent
    data class ToggleIngredient(val index: Int) : RecipeIntent
    data class ToggleStep(val index: Int) : RecipeIntent
    data object ResetIngredients : RecipeIntent
    data object ResetSteps : RecipeIntent
    data object StartCooking : RecipeIntent
    data object StopCooking : RecipeIntent
    data object PauseTimer : RecipeIntent
    data object ResumeTimer : RecipeIntent
    data object ResetTimer : RecipeIntent

}

internal sealed interface RecipeEvent {

    data class NavigateToProfile(val userId: Int) : RecipeEvent
    data class Share(val text: String) : RecipeEvent
    data object CookingDone : RecipeEvent

}
