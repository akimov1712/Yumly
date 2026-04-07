package ru.topbun.home

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.topbun.core.android.MVI
import ru.topbun.core.android.SnackbarManager
import ru.topbun.core.common.error.DataError
import ru.topbun.core.common.onError
import ru.topbun.core.common.onSuccess
import ru.topbun.domain.ScreenUiState
import ru.topbun.domain.entity.recipe.getRecipe.GetRecipeEntity
import ru.topbun.domain.entity.recipe.getRecipe.GetRecipeFilterEntity
import ru.topbun.domain.useCases.recipe.GetRecipeUseCase

internal class HomeViewModel(
    private val getRecipeUseCase: GetRecipeUseCase,
    private val snackbarManager: SnackbarManager
): MVI<HomeIntent, HomeState, HomeEvent>(HomeState()) {

    private var recipeLoadJob: Job? = null

    private fun changeSearch(value: String){ _state.update { it.copy(search = value) } }
    private fun changeSearchType(index: Int){ _state.update { it.copy(selectedSearchTypeIndex = index) } }
    private fun changeShowFilterDialog(value: Boolean){ _state.update { it.copy(showFilterDialog = value) } }
    private fun changeRecipeFilter(filters: GetRecipeFilterEntity){ _state.update { it.copy(recipeFilters = filters) } }

    private fun loadRecipes() = with(state.value){
        recipeLoadJob?.cancel()
        recipeLoadJob = viewModelScope.launch(SupervisorJob()) {
            _state.update { it.copy(recipeList = recipeList.copy(status = ScreenUiState.Loading)) }
            val getRecipeEntity = GetRecipeEntity(q = search, offset = recipeList.recipes.size, recipeFilter = recipeFilters)
            getRecipeUseCase(getRecipeEntity).onSuccess { recipes ->
                _state.update { it.copy(
                    recipeList = recipeList.copy(
                        recipes = it.recipeList.recipes + recipes,
                        status = ScreenUiState.Success,
                        isEndList = recipes.isEmpty(),
                    ))
                }
            }.onError { error, _ ->
                val message = error.toMessage()
                snackbarManager.showMessage(message)
                _state.update { it.copy(recipeList = recipeList.copy(status = ScreenUiState.Error)) }
            }
        }
    }

    private fun observeRecipeChanges() {
        viewModelScope.launch {
            combine(
                state.map { it.search }.distinctUntilChanged(),
                state.map { it.selectedSearchTypeIndex }.distinctUntilChanged(),
                state.map { it.recipeFilters }.distinctUntilChanged()
            ) { search, type, filters ->
                Triple(search, type, filters)
            }.debounce(500).collect {
                refreshRecipes()
            }
        }
    }

    private fun refreshRecipes(){
        _state.update { it.copy(recipeList = HomeState.RecipeListUiState()) }
        loadRecipes()
    }

    override suspend fun handleIntent(intent: HomeIntent) {
        when(intent){
            is HomeIntent.ChangeSearch -> changeSearch(intent.value)
            is HomeIntent.ChangeSearchType -> changeSearchType(intent.index)
            is HomeIntent.ChangeShowFilterDialog -> changeShowFilterDialog(intent.value)
            is HomeIntent.ChangeRecipeFilter -> changeRecipeFilter(intent.filters)
            HomeIntent.LoadRecipe -> loadRecipes()
            HomeIntent.RefreshRecipe -> refreshRecipes()
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

    init {
        observeRecipeChanges()
    }

}
