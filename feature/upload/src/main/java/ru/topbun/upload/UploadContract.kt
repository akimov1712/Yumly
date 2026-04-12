package ru.topbun.upload

import android.net.Uri

internal data class UploadState(
    val preview: Uri? = null,
    val name: String = "",
    val description: String = "",
    val cookingTime: Int = 30,
    val nutrients: Map<NutrientsEnum, Int> = NutrientsEnum.createNutrientMap()
){

    val totalCalories: Int
        get() = nutrients.map { (nutrient, value) -> value * nutrient.calories }.sum()

    val sumLimitExceeded: Boolean
        get() = nutrients.map { (_, value) -> value }.sum() >= 100

    enum class NutrientsEnum(
        val title: String,
        val calories: Int,
    ){
        Protein("Белки", 4),
        Fat("Жиры", 9),
        Carbs("Углеводы", 4);

        companion object{
            fun createNutrientMap() = entries.associateWith { 0 }
        }

    }

}

internal sealed interface UploadIntent{

    data class ChangePreview(val uri: Uri?): UploadIntent
    data class ChangeName(val value: String): UploadIntent
    data class ChangeDescription(val value: String): UploadIntent
    data class ChangeCookingTime(val minutes: Int): UploadIntent
    data class ChangeNutrientValue(val nutrient: UploadState.NutrientsEnum, val value: Int): UploadIntent

}

internal sealed interface UploadEvent{


}