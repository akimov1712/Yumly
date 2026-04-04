package ru.topbun.domain.entity.recipe.getRecipe

import ru.topbun.domain.entity.recipe.RecipeDifficulty
import javax.swing.text.html.parser.Entity

data class GetRecipeFilterEntity(
    val tagIds: List<Int> = emptyList(),
    val cookingTime: Int? = null,
    val minKcal: Int? = null,
    val maxKcal: Int? = null,
    val difficulty: RecipeDifficulty? = null
)
