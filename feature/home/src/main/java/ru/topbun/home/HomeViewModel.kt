package ru.topbun.home

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.topbun.core.android.MVI
import ru.topbun.core.android.SnackbarManager
import ru.topbun.core.common.error.DataError
import ru.topbun.core.common.onError
import ru.topbun.core.common.onSuccess
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

    private fun loadRecipes() = with(state.value){
        viewModelScope.launch{
            recipeLoadJob?.cancel()
            recipeLoadJob = viewModelScope.launch(SupervisorJob()) {
                val getRecipeData = GetRecipeEntity(
                    q = search,
                    offset = recipes.size,
                    recipeFilter = GetRecipeFilterEntity()
                )
                val result = getRecipeUseCase(getRecipeData)
                result.onSuccess { recipes ->
                    _state.update { it.copy(recipes = recipes) }
                }.onError { error, _ ->
                    val message = when(error){
                        DataError.Network.BAD_REQUEST -> "Проверьте корректность введеных данных"
                        DataError.Network.REQUEST_TIMEOUT -> "Время ожидание превышено. Проверьте интернет соединение или попробуйте позже"
                        DataError.Network.SERIALIZATION -> "При получении данных произошла ошибка"
                        DataError.Network.SERVER_ERROR -> "Произошла ошибка на сервере. Попробуйте позже"
                        DataError.Network.NO_INTERNET -> "Отсутствует интернет соединение"
                        else -> "Произошла ошибка. Попробуйте позже"
                    }
                    snackbarManager.showMessage(message)
                }
            }
        }
    }

    override suspend fun handleIntent(intent: HomeIntent) {
        when(intent){
            is HomeIntent.ChangeSearch -> changeSearch(intent.value)
            is HomeIntent.ChangeSearchType -> changeSearchType(intent.index)
        }
    }

    init {
        loadRecipes()
    }

}