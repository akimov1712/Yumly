package ru.topbun.home

internal data class HomeState(
    val search: String = "",
    val selectedSearchTypeIndex: Int = 0,
    val searchTypeList: List<SearchType> = SearchType.entries,
){

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