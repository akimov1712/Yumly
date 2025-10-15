package ru.topbun.data.source.remote.dto.recipe.addRecipe

import ru.topbun.data.source.remote.dto.recipe.IngredientDto
import ru.topbun.data.source.remote.dto.recipe.StepDto
import ru.topbun.data.source.remote.dto.recipe.toDto
import ru.topbun.domain.entity.recipe.IngredientEntity
import ru.topbun.domain.entity.recipe.StepEntity
import ru.topbun.domain.entity.recipe.addRecipe.AddRecipeEntity

data class AddRecipeRequest(
    val title: String,
    val description: String?,
    val previewUrl: String?,
    val cookingTime: Int,
    val kcal: Int,
    val protein: Double,
    val fat: Double,
    val carb: Double,
    val ingredients: List<IngredientDto>,
    val steps: List<StepDto>,
    val tagIds: List<Int>,
)

internal fun AddRecipeEntity.toRequest() = AddRecipeRequest(
    title = title,
    description = description,
    previewUrl = previewUrl,
    cookingTime = cookingTime,
    kcal = kcal,
    protein = protein,
    fat = fat,
    carb = carb,
    ingredients = ingredients.map { it.toDto() },
    steps = steps.map { it.toDto() },
    tagIds = tagIds,
)