package ru.topbun.domain.entity.recipe

import ru.topbun.domain.entity.account.ProfileEntity
import ru.topbun.domain.entity.recipe.tag. TagRecipeEntity

data class RecipeEntity(
    val id: Int,
    val author: ProfileEntity,
    val title: String,
    val description: String?,
    val largeImage: String?,
    val smallImage: String?,
    val isFavorite: Boolean,
    val difficulty: RecipeDifficulty,
    val cookingTime: Int,
    val kcal: Int,
    val protein: Double,
    val fat: Double,
    val carb: Double,
    val tags: List<TagRecipeEntity>,
    val ingredients: List<IngredientEntity>,
    val steps: List<StepEntity>,
)
