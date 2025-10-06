package ru.topbun.domain.entity.recipe.addRecipe

import ru.topbun.domain.entity.recipe.IngredientEntity
import ru.topbun.domain.entity.recipe.StepEntity

data class AddRecipeEntity(
    val title: String,
    val description: String?,
    val previewUrl: String?,
    val cookingTime: Int,
    val kcal: Int,
    val protein: Double,
    val fat: Double,
    val carb: Double,
    val ingredients: List<IngredientEntity>,
    val steps: List<StepEntity>,
    val tagIds: List<Int>,
)
