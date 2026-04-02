package ru.topbun.home

import kotlinx.coroutines.flow.update
import ru.topbun.core.android.MVI

internal class HomeViewModel(

): MVI<HomeIntent, HomeState, HomeEvent>(HomeState()) {

    private fun changeSearch(value: String){ _state.update { it.copy(search = value) } }
    private fun changeSearchType(index: Int){ _state.update { it.copy(selectedSearchTypeIndex = index) } }

    override suspend fun handleIntent(intent: HomeIntent) {
        when(intent){
            is HomeIntent.ChangeSearch -> changeSearch(intent.value)
            is HomeIntent.ChangeSearchType -> changeSearchType(intent.index)
        }
    }

}