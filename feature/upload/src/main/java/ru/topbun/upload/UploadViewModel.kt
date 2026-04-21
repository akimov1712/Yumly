package ru.topbun.upload

import android.net.Uri
import kotlinx.coroutines.flow.update
import ru.topbun.core.android.MVI
import ru.topbun.core.android.SnackbarManager
import ru.topbun.core.common.error.DataError
import ru.topbun.core.common.onError
import ru.topbun.core.common.onSuccess
import ru.topbun.domain.entity.recipe.IngredientEntity
import ru.topbun.domain.entity.recipe.RecipeDifficulty
import ru.topbun.domain.entity.recipe.StepEntity
import ru.topbun.domain.entity.recipe.addRecipe.AddRecipeEntity
import ru.topbun.domain.useCases.recipe.AddRecipeUseCase
import ru.topbun.domain.useCases.session.HasSessionUseCase
import ru.topbun.domain.useCases.upload.UploadFileUseCase
import ru.topbun.upload.fragments.UploadFragments

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

        val newStep = StepEntity(
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

    private suspend fun uploadImage(fileUri: String): String?{
        val result = uploadFileUseCase(fileUri)
        result.onSuccess {
             return it
        }.onError { error, _ ->
            val message = when(error){
                DataError.Network.UNAUTHORIZED -> "Пользователь не авторизован"
                DataError.Network.INVALID_DATA -> "Файл превышает размер 8 мб, либо не верный формат файла"
                DataError.Network.REQUEST_TIMEOUT -> "Время ожидания превышено. Проверьте интернет-соединение или попробуйте позже"
                DataError.Network.SERIALIZATION -> "При получении данных произошла ошибка"
                DataError.Network.SERVER_ERROR -> "Произошла ошибка на сервере. Попробуйте позже"
                DataError.Network.NO_INTERNET -> "Отсутствует интернет-соединение"
                else -> "Произошла ошибка. Попробуйте позже"
            }
            snackbarManager.showMessage(message)
        }
        return null
    }

    private suspend fun publishRecipe() = with(_state.value){
        _state.update { it.copy(publishLoading = true) }
        val previewUrl = preview?.let { uploadImage(it.toString()) ?: return@with }
        val protein = nutrients.getValue(UploadState.NutrientsEnum.Protein).toDouble()
        val fat = nutrients.getValue(UploadState.NutrientsEnum.Fat).toDouble()
        val carbs = nutrients.getValue(UploadState.NutrientsEnum.Carbs).toDouble()
        val steps = steps.map {
            val previewUrl = it.previewUrl?.let { uploadImage(it) ?: return@with }
            StepEntity( description = it.description, previewUrl = previewUrl)
        }
        val recipe = AddRecipeEntity(
            previewUrl = previewUrl,
            title = name,
            description = description,
            cookingTime = cookingTime,
            kcal = totalCalories,
            protein = protein,
            fat = fat,
            carb = carbs,
            ingredients = ingredients,
            steps = steps,
            tagIds = emptyList(),
        )
        val result = addRecipeUseCase(recipe)
        result.onSuccess {
            snackbarManager.showMessage("Рецепт добавлен")
            _state.update { UploadState() }
        }.onError { error, _ ->
            val message = when(error){
                DataError.Network.UNAUTHORIZED -> "Пользователь не авторизован"
                DataError.Network.INVALID_DATA -> "Введённые данные не прошли валидацию. Проверьте их корректность"
                DataError.Network.REQUEST_TIMEOUT -> "Время ожидания превышено. Проверьте интернет-соединение или попробуйте позже"
                DataError.Network.SERIALIZATION -> "При получении данных произошла ошибка"
                DataError.Network.SERVER_ERROR -> "Произошла ошибка на сервере. Попробуйте позже"
                DataError.Network.NO_INTERNET -> "Отсутствует интернет-соединение"
                else -> "Произошла ошибка. Попробуйте позже"
            }
            snackbarManager.showMessage(message)
            _state.update { it.copy(publishLoading = false) }
        }
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
            UploadIntent.PublishRecipe -> publishRecipe()
            UploadIntent.CheckSession -> checkSession()
        }
    }
}
