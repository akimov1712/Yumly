package ru.topbun.home

import androidx.compose.foundation.lazy.LazyListState
import ru.topbun.domain.ScreenUiState
import ru.topbun.domain.entity.recipe.RecipeEntity
import ru.topbun.domain.entity.recipe.getRecipe.GetRecipeFilterEntity

internal data class HomeState(
    val search: String = "",
    val recipeList: RecipeListUiState = RecipeListUiState(),
    val recipeListState: LazyListState = LazyListState(),
    val selectedSearchTypeIndex: Int = 0,
    val searchTypeList: List<SearchType> = SearchType.entries,
    val recipeFilters: GetRecipeFilterEntity = GetRecipeFilterEntity(),
    val showFilterDialog: Boolean = true,

){

    val isFilterChanged: Boolean
        get() = recipeFilters != GetRecipeFilterEntity()

    val searchTypeVisible: Boolean
        get() = recipeListState.firstVisibleItemIndex < 2

    enum class SearchType(val title: String){
        All("All"), Subscribers("Subscribers");
    }

    data class RecipeListUiState(
        val recipes: List<RecipeEntity> = emptyList(),
        val status: ScreenUiState = ScreenUiState.Idle,
        val isEndList: Boolean = false
    )

}

internal sealed interface HomeIntent{

    data class ChangeSearch(val value: String): HomeIntent
    data class ChangeSearchType(val index: Int): HomeIntent
    data class ChangeRecipeFilter(val filters: GetRecipeFilterEntity): HomeIntent
    data class ChangeShowFilterDialog(val value: Boolean): HomeIntent
    data object LoadRecipe: HomeIntent
    data object RefreshRecipe: HomeIntent

}

internal sealed interface HomeEvent{

}
