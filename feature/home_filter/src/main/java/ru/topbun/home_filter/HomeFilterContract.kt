package ru.topbun.home_filter

import ru.topbun.domain.entity.recipe.tag.TagRecipeEntity

internal data class HomeFilterState(
    val categories: List<TagRecipeEntity> = emptyList(),
    val selectedCategoriesIds: List<Int> = emptyList(),
    val isExpanded: Boolean = false,

    val durationProgress: Float = 1f,
    val minDurationLimit: Int = 10,
    val maxDurationLimit: Int = 60,

    val minCaloriesProgress: Float = 0f,
    val maxCaloriesProgress: Float = 1f,

    val minCaloriesLimit: Int = 0,
    val maxCaloriesLimit: Int = 1000,

    val categoryUiState: CategoryUiState = CategoryUiState.Idle
){

    val minCaloriesFromProgress: Int
        get() = (minCaloriesLimit + minCaloriesProgress.coerceIn(0f, 1f) * (maxCaloriesLimit - minCaloriesLimit)).toInt()

    val maxCaloriesFromProgress: Int
        get() = (minCaloriesLimit + maxCaloriesProgress.coerceIn(0f, 1f) * (maxCaloriesLimit - minCaloriesLimit)).toInt()

    val durationFromProgress : Int
        get() {
        val clamped = durationProgress.coerceIn(0f, 1f)
        val range = maxDurationLimit - minDurationLimit

        return (minDurationLimit + clamped * range)
            .toInt()
    }

    val sortedCategories: List<TagRecipeEntity>
        get() = categories.toList().sortedByDescending { selectedCategoriesIds.contains(it.id) }


    enum class CategoryUiState{
        Idle, Loading, Error, Success
    }

}

internal sealed interface HomeFilterIntent{

    object LoadCategories: HomeFilterIntent
    object ChangeExpandedCategoryList: HomeFilterIntent
    data class ChangeSelectedCategory(val id: Int): HomeFilterIntent
    data class ChangeDuration(val progress: Float): HomeFilterIntent
    data class ChangeLimitCalories(val minProgress: Float, val maxProgress: Float): HomeFilterIntent

}

internal sealed interface HomeFilterEvent{

}