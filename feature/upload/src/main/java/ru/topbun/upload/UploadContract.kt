package ru.topbun.upload

import android.net.Uri
import ru.topbun.domain.entity.recipe.IngredientEntity
import ru.topbun.domain.entity.recipe.RecipeDifficulty
import ru.topbun.domain.entity.recipe.StepEntity
import ru.topbun.upload.fragments.UploadFragments

internal data class UploadState(
    val selectedFragment: UploadFragments = UploadFragments.Basic,
    val fragments: List<UploadFragments> = UploadFragments.entries,
    val showDialogClearData: Boolean = false,
    val showDialogAddIngredient: Boolean = false,
    val showDialogAddStep: Boolean = false,

    val preview: Uri? = null,
    val name: String = "",
    val description: String = "",
    val cookingTime: Int = 0,
    val nutrients: Map<NutrientsEnum, Int> = NutrientsEnum.createNutrientMap(),
    val difficultyList: List<RecipeDifficulty> = RecipeDifficulty.entries,
    val selectedDifficultyIndex: Int? = null,
    val ingredients: List<IngredientEntity> = emptyList(),
    val steps: List<StepEntity> = emptyList(),

    val uploadUiState: UploadUiState? = null,
    val publishLoading: Boolean = false,
    val publishRecipeUiState: PublishRecipeUiState = PublishRecipeUiState.None,
){

    val publishedRecipeId: Int?
        get() = if (publishRecipeUiState is PublishRecipeUiState.Success) publishRecipeUiState.id else null

    val publishButtonEnabled: Boolean
        get() = nextButtonEnabled && listOf(ingredients, steps).all { it.isNotEmpty() }

    val nextButtonEnabled: Boolean
        get() = name.isNotBlank()
                && cookingTime != 0
                && nutrients != NutrientsEnum.createNutrientMap()
                && selectedDifficultyIndex != null

    val selectedOrderFragments: Int
        get() = UploadFragments.entries.indexOf(selectedFragment) + 1

    val totalCalories: Int
        get() = nutrients.map { (nutrient, value) -> value * nutrient.calories }.sum()

    val sumLimitExceeded: Boolean
        get() = nutrients.map { (_, value) -> value }.sum() >= 100

    enum class NutrientsEnum(
        val title: String,
        val calories: Int,
    ){
        Protein("Белки", PROTEIN_CALORIES_VALUE),
        Fat("Жиры", FAT_CALORIES_VALUE),
        Carbs("Углеводы", CARBS_CALORIES_VALUE);

        companion object{
            fun createNutrientMap() = entries.associateWith { 0 }
        }

    }

    enum class UploadUiState{
        SUCCESS, NEED_AUTH
    }

    sealed interface PublishRecipeUiState{
        object None: PublishRecipeUiState
        data class Success(val id: Int): PublishRecipeUiState
    }

    companion object{
        private const val PROTEIN_CALORIES_VALUE = 4
        private const val FAT_CALORIES_VALUE = 9
        private const val CARBS_CALORIES_VALUE = 4
    }

}

internal sealed interface UploadIntent{

    data object PublishRecipe: UploadIntent
    data object ClearData: UploadIntent
    data object CheckSession: UploadIntent
    data class ChangeShowDialogClearData(val value: Boolean): UploadIntent
    data class ChangeShowDialogAddIngredient(val value: Boolean): UploadIntent
    data class ChangeShowDialogAddStep(val value: Boolean): UploadIntent
    data class ChangeShowDialogSuccessPublish(val recipeId: Int?): UploadIntent

    data class ChangePreview(val uri: Uri?): UploadIntent
    data class ChangeName(val value: String): UploadIntent
    data class ChangeDescription(val value: String): UploadIntent
    data class ChangeCookingTime(val minutes: Int): UploadIntent
    data class ChangeNutrientValue(val nutrient: UploadState.NutrientsEnum, val value: Int): UploadIntent
    data class ChangeSelectDifficultyIndex(val index: Int): UploadIntent
    data class ChangeFragment(val fragment: UploadFragments): UploadIntent
    data class AddIngredient(val name: String, val value: String): UploadIntent
    data class RemoveIngredient(val index: Int): UploadIntent
    data class ReorderIngredient(val fromIndex: Int, val toIndex: Int): UploadIntent
    data class AddStep(val description: String, val previewUri: String?): UploadIntent
    data class RemoveStep(val index: Int): UploadIntent
    data class ReorderStep(val fromIndex: Int, val toIndex: Int): UploadIntent

}

internal sealed interface UploadEvent{

}
