package ru.topbun.home

import androidx.compose.foundation.lazy.LazyListState
import ru.topbun.domain.entity.recipe.RecipeEntity

internal data class HomeState(
    val search: String = "",
    val recipes: List<RecipeEntity> = emptyList(),
    val recipeListState: LazyListState = LazyListState(),
    val selectedSearchTypeIndex: Int = 0,
    val searchTypeList: List<SearchType> = SearchType.entries,

){

    val searchTypeVisible: Boolean
        get() = recipeListState.firstVisibleItemIndex < 2

    enum class SearchType(val title: String){
        All("All"), Subscribers("Subscribers");
    }

}

internal sealed interface HomeIntent{

    data class ChangeSearch(val value: String): HomeIntent
    data class ChangeSearchType(val index: Int): HomeIntent

}

internal sealed interface HomeEvent{

}