package ru.topbun.home_filter

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
import ru.topbun.domain.ScreenUiState
import ru.topbun.domain.entity.recipe.RecipeDifficulty
import ru.topbun.domain.entity.recipe.getRecipe.GetRecipeFilterEntity
import ru.topbun.domain.useCases.recipe.GetTagsUseCase

internal class HomeFilterViewModel(
    private val getTagsUseCase: GetTagsUseCase,
    private val snackbarManager: SnackbarManager
): MVI<HomeFilterIntent, HomeFilterState, HomeFilterEvent>(HomeFilterState()) {

    private var loadCategoryJob: Job? = null

    private fun changeExpandedCategoryList() = _state.update { it.copy(isExpanded = !_state.value.isExpanded) }
    private fun changeDuration(progress: Float) = _state.update { it.copy(durationProgress = progress) }
    private fun changeLimitCalories(minProgress: Float, maxProgress: Float){ _state.update { it.copy(minCaloriesProgress = minProgress, maxCaloriesProgress = maxProgress) } }
    private fun changeSelectDifficultyIndex(index: Int) = _state.update { it.copy(selectedDifficultyIndex = index.takeIf { _state.value.selectedDifficultyIndex != index }) }

    private fun onClickClear() = _state.update {
        it.copy(
            selectedCategoriesIds = emptyList(),
            selectedDifficultyIndex = null,
            durationProgress = 1f,
            minCaloriesProgress = 0f,
            maxCaloriesProgress = 1f
        )
    }
    private suspend fun onClickDone() = with(state.value){
        val filters = GetRecipeFilterEntity(
            tagIds = selectedCategoriesIds,
            cookingTime = durationFromProgress.takeIf { durationProgress != 1f },
            minKcal = minCaloriesFromProgress.takeIf { minCaloriesProgress != 0f },
            maxKcal = maxCaloriesFromProgress.takeIf { maxCaloriesProgress != 1f },
            difficulty = selectedDifficultyIndex?.let { difficultyList[it] }
        )
        _events.send(HomeFilterEvent.ApplyFilters(filters))
    }


    private fun changeSelectedCategory(id: Int): Unit = with(_state.value){
        val newSelectedList = selectedCategoriesIds.toMutableList().apply { if (selectedCategoriesIds.contains(id)) remove(id) else add(id) }
        _state.update {
            it.copy(selectedCategoriesIds = newSelectedList,)
        }
    }

    private fun loadCategories(): Unit = with(_state){
        loadCategoryJob?.cancel()
        loadCategoryJob = viewModelScope.launch(SupervisorJob()) {
            update{ it.copy(categoryUiState = ScreenUiState.Loading) }
            val result = getTagsUseCase()
            result.onSuccess { tags ->
                update{ it.copy(categories = tags, categoryUiState = ScreenUiState.Success) }
            }.onError { error, _ ->
                val message = when(error){
                    DataError.Network.REQUEST_TIMEOUT -> "Время ожидание превышено. Проверьте интернет соединение или попробуйте позже"
                    DataError.Network.SERIALIZATION -> "При получении данных произошла ошибка"
                    DataError.Network.SERVER_ERROR -> "Произошла ошибка на сервере. Попробуйте позже"
                    DataError.Network.NO_INTERNET -> "Отсутствует интернет соединение"
                    else -> "Произошла ошибка. Попробуйте позже"
                }
                snackbarManager.showMessage(message)
                update{ it.copy(categoryUiState = ScreenUiState.Error) }
            }
        }
    }

    private fun initFilter(filter: GetRecipeFilterEntity){
        _state.update {
            it.copy(
                selectedCategoriesIds = filter.tagIds,
                selectedDifficultyIndex = filter.difficulty?.let { RecipeDifficulty.entries.indexOf(it) },
                durationProgress = filter.cookingTime?.let { HomeFilterState.progressFromDuration(it) } ?: 1f,
                minCaloriesProgress = filter.minKcal?.let { HomeFilterState.progressFromMinCalories(it) } ?: 0f,
                maxCaloriesProgress = filter.maxKcal?.let { HomeFilterState.progressFromMaxCalories(it) } ?: 1f,
            )
        }
    }


    override suspend fun handleIntent(intent: HomeFilterIntent) {
        when(intent){
            is HomeFilterIntent.InitFilters -> initFilter(intent.filter)
            HomeFilterIntent.ChangeExpandedCategoryList -> changeExpandedCategoryList()
            is HomeFilterIntent.ChangeSelectedCategory -> changeSelectedCategory(intent.id)
            HomeFilterIntent.LoadCategories -> loadCategories()
            is HomeFilterIntent.ChangeDuration -> changeDuration(intent.progress)
            is HomeFilterIntent.ChangeLimitCalories -> changeLimitCalories(intent.minProgress, intent.maxProgress)
            is HomeFilterIntent.ChangeSelectDifficultyIndex -> changeSelectDifficultyIndex(intent.index)
            HomeFilterIntent.ClickClear -> onClickClear()
            HomeFilterIntent.ClickDone -> onClickDone()
        }
    }

    init {
        loadCategories()
    }


}