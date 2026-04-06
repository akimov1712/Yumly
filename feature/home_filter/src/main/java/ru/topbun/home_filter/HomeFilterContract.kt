package ru.topbun.home_filter

import ru.topbun.domain.entity.recipe.RecipeDifficulty
import ru.topbun.domain.entity.recipe.getRecipe.GetRecipeFilterEntity
import ru.topbun.domain.entity.recipe.tag.TagRecipeEntity

internal data class HomeFilterState(
    val categories: List<TagRecipeEntity> = emptyList(),
    val selectedCategoriesIds: List<Int> = emptyList(),
    val isExpanded: Boolean = false,
    val durationProgress: Float = 1f,
    val minCaloriesProgress: Float = 0f,
    val maxCaloriesProgress: Float = 1f,
    val difficultyList: List<RecipeDifficulty> = RecipeDifficulty.entries,
    val selectedDifficultyIndex: Int? = null,
    val categoryUiState: CategoryUiState = CategoryUiState.Idle
){

    val minCaloriesFromProgress: Int
        get() = (MIN_CALORIES_LIMIT + minCaloriesProgress.coerceIn(0f, 1f) * (MAX_CALORIES_LIMIT - MIN_CALORIES_LIMIT)).toInt()

    val maxCaloriesFromProgress: Int
        get() = (MIN_CALORIES_LIMIT + maxCaloriesProgress.coerceIn(0f, 1f) * (MAX_CALORIES_LIMIT - MIN_CALORIES_LIMIT)).toInt()

    val durationFromProgress : Int
        get() {
            val clamped = durationProgress.coerceIn(0f, 1f)
            val range = MAX_DURATION_LIMIT - MIN_DURATION_LIMIT

            return (MIN_DURATION_LIMIT + clamped * range)
                .toInt()
        }

    val sortedCategories: List<TagRecipeEntity>
        get() = categories.toList().sortedByDescending { selectedCategoriesIds.contains(it.id) }

    enum class CategoryUiState{
        Idle, Loading, Error, Success
    }

    companion object {

        internal fun progressFromMinCalories(calories: Int): Float {
            val clamped = calories.coerceIn(MIN_CALORIES_LIMIT, MAX_CALORIES_LIMIT)
            return (clamped - MIN_CALORIES_LIMIT).toFloat() /
                    (MAX_CALORIES_LIMIT - MIN_CALORIES_LIMIT)
        }

        internal fun progressFromMaxCalories(calories: Int): Float {
            val clamped = calories.coerceIn(MIN_CALORIES_LIMIT, MAX_CALORIES_LIMIT)
            return (clamped - MIN_CALORIES_LIMIT).toFloat() /
                    (MAX_CALORIES_LIMIT - MIN_CALORIES_LIMIT)
        }

        internal fun progressFromDuration(duration: Int): Float {
            val clamped = duration.coerceIn(MIN_DURATION_LIMIT, MAX_DURATION_LIMIT)
            return (clamped - MIN_DURATION_LIMIT).toFloat() /
                    (MAX_DURATION_LIMIT - MIN_DURATION_LIMIT)
        }

        internal const val MIN_DURATION_LIMIT: Int = 10
        internal const val MAX_DURATION_LIMIT: Int = 60

        internal const val MIN_CALORIES_LIMIT: Int = 0
        internal const val MAX_CALORIES_LIMIT: Int = 1000
    }

}

internal sealed interface HomeFilterIntent{

    data class InitFilters(val filter: GetRecipeFilterEntity): HomeFilterIntent
    object LoadCategories: HomeFilterIntent
    object ChangeExpandedCategoryList: HomeFilterIntent
    data class ChangeSelectedCategory(val id: Int): HomeFilterIntent
    data class ChangeDuration(val progress: Float): HomeFilterIntent
    data class ChangeLimitCalories(val minProgress: Float, val maxProgress: Float): HomeFilterIntent
    data class ChangeSelectDifficultyIndex(val index: Int): HomeFilterIntent

    data object ClickClear: HomeFilterIntent
    data object ClickDone: HomeFilterIntent

}

internal sealed interface HomeFilterEvent{
    data class ApplyFilters(val filters: GetRecipeFilterEntity): HomeFilterEvent
}