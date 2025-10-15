package ru.topbun.domain.entity.recipe.getRecipe

import ru.topbun.domain.entity.recipe.RecipeDifficulty
import javax.swing.text.html.parser.Entity

data class GetRecipeFilterEntity(
    val tagIds: List<Int>,
    val cookingTime: Int?,
    val minKcal: Int?,
    val maxKcal: Int?,
    val difficulty: RecipeDifficulty?
)
