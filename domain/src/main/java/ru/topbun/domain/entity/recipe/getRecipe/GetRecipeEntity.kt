package ru.topbun.domain.entity.recipe.getRecipe

data class GetRecipeEntity(
    val q: String?,
    val offset: Int? = null,
    val limit: Int? = null,
    val recipeFilter: GetRecipeFilterEntity
)
