package ru.topbun.home_filter

import ru.topbun.domain.entity.recipe.tag.TagRecipeEntity

internal data class HomeFilterState(
    val categories: List<TagRecipeEntity> = emptyList(),
    val selectedCategoriesIds: List<Int> = emptyList(),
    val isExpanded: Boolean = false,
    val categoryUiState: CategoryUiState = CategoryUiState.Idle
){

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

}

internal sealed interface HomeFilterEvent{

}