package ru.topbun.upload

import android.net.Uri
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.topbun.core.android.MVI
import ru.topbun.core.android.SnackbarManager
import ru.topbun.domain.entity.recipe.IngredientEntity
import ru.topbun.domain.entity.recipe.RecipeDifficulty
import ru.topbun.domain.entity.recipe.StepEntity
import ru.topbun.domain.useCases.recipe.AddRecipeUseCase
import ru.topbun.domain.useCases.session.HasSessionUseCase
import ru.topbun.domain.useCases.upload.UploadFileUseCase
import ru.topbun.upload.fragments.UploadFragments
import ru.topbun.upload.fragments.UploadFragments.Basic
import ru.topbun.upload.fragments.UploadFragments.Content

internal class UploadViewModel(
    private val hasSessionUseCase: HasSessionUseCase,
    private val addRecipeUseCase: AddRecipeUseCase,
    private val uploadFileUseCase: UploadFileUseCase,
    private val snackbarManager: SnackbarManager
): MVI<UploadIntent, UploadState, UploadEvent>(UploadState()){

    private fun changePreview(uri: Uri?) = _state.update { it.copy(preview = uri) }
    private fun changeTitle(value: String){ if (value.length <= 72) _state.update { it.copy(name = value) } }
    private fun changeDescription(value: String){ if (value.length <= 500) _state.update { it.copy(description = value) } }
    private fun changeCookingTime(minutes: Int) = _state.update { it.copy(cookingTime = minutes) }
    private fun changeSelectDifficultyIndex(index: Int) = _state.update { it.copy(selectedDifficultyIndex = index.takeIf { _state.value.selectedDifficultyIndex != index }) }
    private fun changeFragment(fragment: UploadFragments) = _state.update { it.copy(selectedFragment = fragment) }
    private fun changeShowDialogClearData(value: Boolean) = _state.update { it.copy(showDialogClearData = value) }
    private fun changeShowDialogAddIngredient(value: Boolean) = _state.update { it.copy(showDialogAddIngredient = value) }
    private fun changeShowDialogAddStep(value: Boolean) = _state.update { it.copy(showDialogAddStep = value) }

    private fun changeNutrientValue(nutrient: UploadState.NutrientsEnum, value: Int){
        val newNutrients = _state.value.nutrients.toMutableMap()
        newNutrients[nutrient] = value
        if (newNutrients.values.sum() <= 100){
            _state.update { it.copy(nutrients = newNutrients) }
        }
    }

    private fun addIngredient(name: String, value: String) {
        val ingredientName = name.trim()
        val ingredientValue = value.trim()

        if (ingredientName.isBlank() || ingredientValue.isBlank()) return
        val newIngredient = IngredientEntity(
            name = ingredientName,
            value = ingredientValue
        )

        _state.update {
            it.copy(
                ingredients = it.ingredients + newIngredient,
                showDialogAddIngredient = false
            )
        }
    }

    private fun removeIngredient(index: Int) = _state.update {
        val newList = it.ingredients.toMutableList()
        if (index !in newList.indices) return@update it
        newList.removeAt(index)
        it.copy(ingredients = newList)
    }

    private fun reorderIngredients(fromIndex: Int, toIndex: Int) = _state.update {
        val currentList = it.ingredients.toMutableList()
        if (fromIndex !in currentList.indices || toIndex !in 0..currentList.size) return@update it

        val item = currentList.removeAt(fromIndex)
        currentList.add(toIndex, item)

        it.copy(ingredients = currentList)
    }

    private fun addStep(description: String, previewUri: String?) {
        val stepDescription = description.trim()
        if (stepDescription.isBlank()) return

        val nextId = (_state.value.steps.maxOfOrNull { it.id } ?: 0) + 1
        val newStep = StepEntity(
            id = nextId,
            description = stepDescription,
            previewUrl = previewUri?.takeIf { it.isNotBlank() }
        )

        _state.update {
            it.copy(
                steps = it.steps + newStep,
                showDialogAddStep = false
            )
        }
    }

    private fun removeStep(index: Int) = _state.update {
        val newList = it.steps.toMutableList()
        if (index !in newList.indices) return@update it
        newList.removeAt(index)
        it.copy(steps = newList)
    }

    private fun reorderSteps(fromIndex: Int, toIndex: Int) = _state.update {
        val currentList = it.steps.toMutableList()
        if (fromIndex !in currentList.indices || toIndex !in 0..currentList.size) return@update it

        val item = currentList.removeAt(fromIndex)
        currentList.add(toIndex, item)

        it.copy(steps = currentList)
    }


    private fun clearData() = with(state.value){
        val newState = copy(
            preview = null,
            name = "",
            description = "",
            cookingTime = 0,
            nutrients = UploadState.NutrientsEnum.createNutrientMap(),
            difficultyList = RecipeDifficulty.entries,
            selectedDifficultyIndex = null,

            ingredients = emptyList(),
            showDialogAddIngredient = false,
            steps = emptyList(),
            showDialogAddStep = false
        )
        _state.update { newState }
    }

    private fun checkSession() {
        val hasSession = hasSessionUseCase()
        val uploadUiState = if (!hasSession) UploadState.UploadUiState.NEED_AUTH else UploadState.UploadUiState.SUCCESS
        _state.update { it.copy(uploadUiState = uploadUiState) }
    }

    private suspend fun publishRecipe() = with(_state.value){
        val previewUrl = preview?.let { uploadFileUseCase(it) }
    }

    override suspend fun handleIntent(intent: UploadIntent) {
        when(intent){
            UploadIntent.ClearData -> clearData()
            is UploadIntent.ChangePreview -> changePreview(intent.uri)
            is UploadIntent.ChangeName -> changeTitle(intent.value)
            is UploadIntent.ChangeDescription -> changeDescription(intent.value)
            is UploadIntent.ChangeCookingTime -> changeCookingTime(intent.minutes)
            is UploadIntent.ChangeNutrientValue -> changeNutrientValue(intent.nutrient, intent.value)
            is UploadIntent.ChangeSelectDifficultyIndex -> changeSelectDifficultyIndex(intent.index)
            is UploadIntent.ChangeFragment -> changeFragment(intent.fragment)
            is UploadIntent.ChangeShowDialogClearData -> changeShowDialogClearData(intent.value)
            is UploadIntent.ChangeShowDialogAddIngredient -> changeShowDialogAddIngredient(intent.value)
            is UploadIntent.ChangeShowDialogAddStep -> changeShowDialogAddStep(intent.value)
            is UploadIntent.AddIngredient -> addIngredient(intent.name, intent.value)
            is UploadIntent.RemoveIngredient -> removeIngredient(intent.index)
            is UploadIntent.ReorderIngredient -> reorderIngredients(intent.fromIndex, intent.toIndex)
            is UploadIntent.AddStep -> addStep(intent.description, intent.previewUri)
            is UploadIntent.RemoveStep -> removeStep(intent.index)
            is UploadIntent.ReorderStep -> reorderSteps(intent.fromIndex, intent.toIndex)
            UploadIntent.PublishRecipe -> {}
            UploadIntent.CheckSession -> checkSession()
        }
    }
}
