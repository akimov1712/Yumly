package ru.topbun.upload

internal data class UploadState(
    val nutrients: Map<NutrientsEnum, Int> = NutrientsEnum.createNutrientMap()
){

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