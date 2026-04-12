package ru.topbun.upload

import android.net.Uri
import android.util.Log
import kotlinx.coroutines.flow.update
import ru.topbun.core.android.MVI

internal class UploadViewModel: MVI<UploadIntent, UploadState, UploadEvent>(UploadState()){

    private fun changePreview(uri: Uri?) = _state.update { it.copy(preview = uri) }
    private fun changeTitle(value: String){ if (value.length <= 72) _state.update { it.copy(name = value) } }
    private fun changeDescription(value: String){ if (value.length <= 500) _state.update { it.copy(description = value) } }
    private fun changeCookingTime(minutes: Int) = _state.update { it.copy(cookingTime = minutes) }
    private fun changeNutrientValue(nutrient: UploadState.NutrientsEnum, value: Int){
        val newNutrients = _state.value.nutrients.toMutableMap()
        newNutrients[nutrient] = value
        if (newNutrients.values.sum() <= 100){
            _state.update { it.copy(nutrients = newNutrients) }
        }
    }


    override suspend fun handleIntent(intent: UploadIntent) {
        when(intent){
            is UploadIntent.ChangePreview -> changePreview(intent.uri)
            is UploadIntent.ChangeName -> changeTitle(intent.value)
            is UploadIntent.ChangeDescription -> changeDescription(intent.value)
            is UploadIntent.ChangeCookingTime -> changeCookingTime(intent.minutes)
            is UploadIntent.ChangeNutrientValue -> changeNutrientValue(intent.nutrient, intent.value)
        }
    }
}