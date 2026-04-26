package ru.topbun.domain.entity.recipe.getRecipe

data class GetRecipeEntity(
    val q: String?,
    val offset: Int = 0,
    val limit: Int = 20,
    val recipeFilter: GetRecipeFilterEntity,
    val onlyFromFollowing: Boolean = false,
)
