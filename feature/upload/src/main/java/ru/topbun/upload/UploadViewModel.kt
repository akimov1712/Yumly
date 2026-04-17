package ru.topbun.upload

import android.net.Uri
import kotlinx.coroutines.flow.update
import ru.topbun.core.android.MVI
import ru.topbun.domain.entity.recipe.RecipeDifficulty
import ru.topbun.upload.fragments.UploadFragments
import ru.topbun.upload.fragments.UploadFragments.Basic
import ru.topbun.upload.fragments.UploadFragments.Content

internal class UploadViewModel: MVI<UploadIntent, UploadState, UploadEvent>(UploadState()){

    private fun changePreview(uri: Uri?) = _state.update { it.copy(preview = uri) }
    private fun changeTitle(value: String){ if (value.length <= 72) _state.update { it.copy(name = value) } }
    private fun changeDescription(value: String){ if (value.length <= 500) _state.update { it.copy(description = value) } }
    private fun changeCookingTime(minutes: Int) = _state.update { it.copy(cookingTime = minutes) }
    private fun changeSelectDifficultyIndex(index: Int) = _state.update { it.copy(selectedDifficultyIndex = index.takeIf { _state.value.selectedDifficultyIndex != index }) }
    private fun changeFragment(fragment: UploadFragments) = _state.update { it.copy(selectedFragment = fragment) }
    private fun changeShowDialogClearData(value: Boolean) = _state.update { it.copy(showDialogClearData = value) }

    private fun changeNutrientValue(nutrient: UploadState.NutrientsEnum, value: Int){
        val newNutrients = _state.value.nutrients.toMutableMap()
        newNutrients[nutrient] = value
        if (newNutrients.values.sum() <= 100){
            _state.update { it.copy(nutrients = newNutrients) }
        }
    }

    private fun clearData() = with(state.value){
        val newState = when (selectedFragment) {
            Basic -> copy(
                preview = null,
                name = "",
                description = "",
                cookingTime = 0,
                nutrients = UploadState.NutrientsEnum.createNutrientMap(),
                difficultyList = RecipeDifficulty.entries,
                selectedDifficultyIndex = null,
            )
            Content -> copy()
        }
        _state.update { newState }
    }


    override suspend fun handleIntent(intent: UploadIntent) {
        when(intent){
            is UploadIntent.ChangePreview -> changePreview(intent.uri)
            is UploadIntent.ChangeName -> changeTitle(intent.value)
            is UploadIntent.ChangeDescription -> changeDescription(intent.value)
            is UploadIntent.ChangeCookingTime -> changeCookingTime(intent.minutes)
            is UploadIntent.ChangeNutrientValue -> changeNutrientValue(intent.nutrient, intent.value)
            is UploadIntent.ChangeSelectDifficultyIndex -> changeSelectDifficultyIndex(intent.index)
            is UploadIntent.ChangeFragment -> changeFragment(intent.fragment)
            UploadIntent.ClearData -> clearData()
            is UploadIntent.ChangeShowDialogClearData -> changeShowDialogClearData(intent.value)
        }
    }
}