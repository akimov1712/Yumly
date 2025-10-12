package ru.topbun.domain.repository.recipe

import ru.topbun.common.error.DataError
import ru.topbun.common.Result
import ru.topbun.domain.entity.recipe.RecipeEntity
import ru.topbun.domain.entity.recipe.addRecipe.AddRecipeEntity
import ru.topbun.domain.entity.recipe.getRecipe.GetRecipeEntity

interface RecipeRepository {

    suspend fun getRecipe(data: GetRecipeEntity): Result<List<RecipeEntity> ,DataError>
    suspend fun getRecipeById(id: Int): Result<RecipeEntity ,DataError>
    suspend fun addRecipe(data: AddRecipeEntity): Result<RecipeEntity, DataError>
    suspend fun deleteRecipe(id: Int): Result<Unit, DataError>

}