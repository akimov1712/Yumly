package ru.topbun.domain.repository.recipe

import ru.topbun.common.DataError
import ru.topbun.common.Result
import ru.topbun.domain.entity.recipe.RecipeEntity
import ru.topbun.domain.entity.recipe.getRecipe.GetRecipeEntity

interface RecipeRepository {

    suspend fun getRecipe(getRecipe: GetRecipeEntity): Result<List<RecipeEntity> ,DataError>

}