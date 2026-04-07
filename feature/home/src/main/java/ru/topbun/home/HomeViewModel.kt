package ru.topbun.home

import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import ru.topbun.core.android.MVI
import ru.topbun.core.android.SnackbarManager
import ru.topbun.core.common.error.DataError
import ru.topbun.domain.entity.recipe.getRecipe.GetRecipeFilterEntity
import ru.topbun.domain.useCases.recipe.GetRecipeUseCase

internal class HomeViewModel(
    private val getRecipeUseCase: GetRecipeUseCase,
    private val snackbarManager: SnackbarManager
): MVI<HomeIntent, HomeState, HomeEvent>(HomeState()) {

    @OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
    val recipes = combine(
        state.map { it.search }.distinctUntilChanged(),
        state.map { it.selectedSearchTypeIndex }.distinctUntilChanged(),
        state.map { it.recipeFilters }.distinctUntilChanged()
    ) { search, type, filters ->
        Triple(search, type, filters)
    }.debounce(500)
        .flatMapLatest { (search, _, filters) ->
            _state.update { it.copy(recipes = emptyList()) }
            Pager(
                config = PagingConfig(
                    pageSize = PAGE_SIZE,
                    initialLoadSize = PAGE_SIZE,
                    prefetchDistance = PAGE_PREFETCH_DISTANCE,
                    enablePlaceholders = false
                )
            ) {
                RecipePagingSource(
                    search = search,
                    recipeFilter = filters,
                    getRecipeUseCase = getRecipeUseCase,
                ) { error ->
                    snackbarManager.showMessage(error.toMessage())
                }
            }.flow
        }
        .cachedIn(viewModelScope)

    private fun changeSearch(value: String){ _state.update { it.copy(search = value) } }
    private fun changeSearchType(index: Int){ _state.update { it.copy(selectedSearchTypeIndex = index) } }
    private fun changeShowFilterDialog(value: Boolean){ _state.update { it.copy(showFilterDialog = value) } }
    private fun changeRecipeFilter(filters: GetRecipeFilterEntity){ _state.update { it.copy(recipeFilters = filters) } }

    override suspend fun handleIntent(intent: HomeIntent) {
        when(intent){
            is HomeIntent.ChangeSearch -> changeSearch(intent.value)
            is HomeIntent.ChangeSearchType -> changeSearchType(intent.index)
            is HomeIntent.ChangeShowFilterDialog -> changeShowFilterDialog(intent.value)
            is HomeIntent.ChangeRecipeFilter -> changeRecipeFilter(intent.filters)
        }
    }

    private fun DataError.toMessage(): String = when(this){
        DataError.Network.BAD_REQUEST,
        DataError.Network.INVALID_DATA -> "Проверьте корректность введённых данных"
        DataError.Network.REQUEST_TIMEOUT -> "Время ожидания превышено. Проверьте интернет-соединение или попробуйте позже"
        DataError.Network.SERIALIZATION -> "При получении данных произошла ошибка"
        DataError.Network.SERVER_ERROR -> "Произошла ошибка на сервере. Попробуйте позже"
        DataError.Network.NO_INTERNET -> "Отсутствует интернет-соединение"
        else -> "Произошла ошибка. Попробуйте позже"
    }

    private companion object {
        const val PAGE_SIZE = 20
        const val PAGE_PREFETCH_DISTANCE = 5
    }

}
