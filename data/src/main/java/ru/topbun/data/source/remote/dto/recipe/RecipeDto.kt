package ru.topbun.data.source.remote.dto.recipe

import ru.topbun.data.source.remote.dto.account.ProfileDto
import ru.topbun.domain.entity.recipe.IngredientEntity
import ru.topbun.domain.entity.recipe.RecipeDifficulty
import ru.topbun.domain.entity.recipe.RecipeEntity
import ru.topbun.domain.entity.recipe.StepEntity
import ru.topbun.domain.entity.recipe.tag.TagRecipeEntity

data class RecipeDto(
    val id: Int,
    val author: ProfileDto,
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
    val tags: List<TagRecipeDto>,
    val ingredients: List<IngredientDto>,
    val steps: List<StepDto>,
){

    fun toEntity() = RecipeEntity(
        id = id,
        author = author.toEntity(),
        title = title,
        description = description,
        largeImage = largeImage,
        smallImage = smallImage,
        isFavorite = isFavorite,
        difficulty = difficulty,
        cookingTime = cookingTime,
        kcal = kcal,
        protein = protein,
        fat = fat,
        carb = carb,
        tags = tags.map { it.toEntity() },
        ingredients = ingredients.map { it.toEntity() },
        steps = steps.map { it.toEntity() },
    )

}