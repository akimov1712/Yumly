package ru.topbun.domain.repository.recipe

import ru.topbun.core.common.Result
import ru.topbun.core.common.error.DataError
import ru.topbun.domain.entity.recipe.RecipeEntity
import ru.topbun.domain.entity.recipe.addRecipe.AddRecipeEntity
import ru.topbun.domain.entity.recipe.getRecipe.GetRecipeByUserIdEntity
import ru.topbun.domain.entity.recipe.getRecipe.GetRecipeEntity
import ru.topbun.domain.entity.recipe.tag.TagRecipeEntity

interface RecipeRepository {

    suspend fun getRecipe(data: GetRecipeEntity): Result<List<RecipeEntity> ,DataError>
    suspend fun getRecipeById(id: Int, fromCache: Boolean): Result<RecipeEntity ,DataError>
    suspend fun getRecipeByUserId(userId: Int, data: GetRecipeByUserIdEntity): Result<List<RecipeEntity> ,DataError>
    suspend fun addRecipe(data: AddRecipeEntity): Result<RecipeEntity, DataError>
    suspend fun deleteRecipe(id: Int): Result<Unit, DataError>
    suspend fun getTags(): Result<List<TagRecipeEntity>, DataError>

}