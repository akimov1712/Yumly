package ru.topbun.data.source.remote.dto.recipe.getRecipe

import ru.topbun.domain.entity.recipe.getRecipe.GetRecipeEntity

data class GetRecipeRequest(
    val q: String?,
    val offset: Int = 0,
    val limit: Int = 20,
    val recipeFilter: GetRecipeFilterDto
)

internal fun GetRecipeEntity.toRequest() = GetRecipeRequest(
    q = q,
    offset = offset,
    limit = limit,
    recipeFilter = recipeFilter.toDto(),
)
