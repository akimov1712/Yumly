package ru.topbun.data.source.remote.dto.recipe.getRecipe

import ru.topbun.domain.entity.recipe.getRecipe.GetRecipeFilterEntity

data class GetRecipeFilterDto(
    val tagIds: List<Int>,
    val cookingTime: Int?,
    val minKcal: Int?,
    val maxKcal: Int?,
    val difficulty: String?
)

internal fun GetRecipeFilterEntity.toDto() = GetRecipeFilterDto(
    tagIds = tagIds,
    cookingTime = cookingTime,
    minKcal = minKcal,
    maxKcal = maxKcal,
    difficulty = difficulty?.toString(),
)