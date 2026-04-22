package ru.topbun.domain.validation.recipe

import ru.topbun.core.common.Result
import ru.topbun.core.common.Validator
import ru.topbun.core.common.error.ValidatorError
import ru.topbun.domain.entity.recipe.addRecipe.AddRecipeEntity

class AddRecipeValidator : Validator<AddRecipeEntity> {

    override fun validate(data: AddRecipeEntity): Result<Unit, AddRecipeValidatorError> {
        val error = when {
            data.title.length > 72 -> AddRecipeValidatorError.TITLE_LENGTH
            (data.description?.length ?: 0) > 500 -> AddRecipeValidatorError.DESCRIPTION_LENGTH
            data.cookingTime > 14400 -> AddRecipeValidatorError.COOKING_TIME
            data.ingredients.size !in (1..32) -> AddRecipeValidatorError.COUNT_INGREDIENTS
            data.steps.size !in (1..32) -> AddRecipeValidatorError.COUNT_STEPS
            data.protein > 100 -> AddRecipeValidatorError.COUNT_PROTEIN
            data.carb > 100 -> AddRecipeValidatorError.COUNT_CARBS
            data.fat > 100 -> AddRecipeValidatorError.COUNT_FAT
            else -> null
        }
        return error?.let { Result.Error(error) } ?: run { Result.Success(Unit) }
    }

}

enum class AddRecipeValidatorError : ValidatorError {

    TITLE_LENGTH, DESCRIPTION_LENGTH, COOKING_TIME, COUNT_INGREDIENTS, COUNT_STEPS,
    COUNT_PROTEIN, COUNT_CARBS, COUNT_FAT

}