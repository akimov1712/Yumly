package ru.topbun.data.source.remote.dto.recipe.getRecipe

import ru.topbun.domain.entity.recipe.getRecipe.GetRecipeByUserIdEntity

data class GetRecipeByUserIdRequest(
    val offset: Int = 0,
    val limit: Int = 20,
)

internal fun GetRecipeByUserIdEntity.toRequest() = GetRecipeByUserIdRequest(
    offset = offset,
    limit = limit,
)