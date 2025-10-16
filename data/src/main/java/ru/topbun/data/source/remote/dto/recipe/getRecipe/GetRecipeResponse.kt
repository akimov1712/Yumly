package ru.topbun.data.source.remote.dto.recipe.getRecipe

import ru.topbun.data.source.remote.dto.recipe.RecipeDto

internal class GetRecipeResponse: ArrayList<RecipeDto>(){

    fun toEntityList() = map { it.toEntity() }

}