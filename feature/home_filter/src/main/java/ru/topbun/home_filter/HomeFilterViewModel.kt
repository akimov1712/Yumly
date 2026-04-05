package ru.topbun.home_filter

import android.provider.SyncStateContract.Helpers.update
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
import ru.topbun.domain.useCases.recipe.GetTagsUseCase

internal class HomeFilterViewModel(
    private val getTagsUseCase: GetTagsUseCase,
    private val snackbarManager: SnackbarManager
): MVI<HomeFilterIntent, HomeFilterState, HomeFilterEvent>( HomeFilterState()) {

    private var loadCategoryJob: Job? = null

    private fun changeExpandedCategoryList() = _state.update { it.copy(isExpanded = !_state.value.isExpanded) }
    private fun changeSelectedCategory(id: Int): Unit = with(_state.value){
        val newSelectedList = selectedCategoriesIds.toMutableList().apply { if (selectedCategoriesIds.contains(id)) remove(id) else add(id) }
        _state.update {
            it.copy(selectedCategoriesIds = newSelectedList,)
        }
    }

    private fun changeDuration(progress: Float) = _state.update { it.copy(durationProgress = progress) }

    private fun loadCategories(): Unit = with(_state){
        loadCategoryJob?.cancel()
        loadCategoryJob = viewModelScope.launch(SupervisorJob()) {
            update{ it.copy(categoryUiState = HomeFilterState.CategoryUiState.Loading) }
            val result = getTagsUseCase()
            result.onSuccess { tags ->
                update{ it.copy(categories = tags, categoryUiState = HomeFilterState.CategoryUiState.Success) }
            }.onError { error, _ ->
                val message = when(error){
                    DataError.Network.REQUEST_TIMEOUT -> "Время ожидание превышено. Проверьте интернет соединение или попробуйте позже"
                    DataError.Network.SERIALIZATION -> "При получении данных произошла ошибка"
                    DataError.Network.SERVER_ERROR -> "Произошла ошибка на сервере. Попробуйте позже"
                    DataError.Network.NO_INTERNET -> "Отсутствует интернет соединение"
                    else -> "Произошла ошибка. Попробуйте позже"
                }
                snackbarManager.showMessage(message)
                update{ it.copy(categoryUiState = HomeFilterState.CategoryUiState.Error) }
            }
        }
    }


    override suspend fun handleIntent(intent: HomeFilterIntent) {
        when(intent){
            HomeFilterIntent.ChangeExpandedCategoryList -> changeExpandedCategoryList()
            is HomeFilterIntent.ChangeSelectedCategory -> changeSelectedCategory(intent.id)
            HomeFilterIntent.LoadCategories -> loadCategories()
            is HomeFilterIntent.ChangeDuration -> changeDuration(intent.progress)
        }
    }

    init {
        loadCategories()
    }


}