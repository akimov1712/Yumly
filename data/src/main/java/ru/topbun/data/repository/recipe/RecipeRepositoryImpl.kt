package ru.topbun.data.repository.recipe

import android.R.attr.data
import android.content.Context
import ru.topbun.common.HttpStatusCode
import ru.topbun.common.Result
import ru.topbun.common.error.DataError
import ru.topbun.data.exceptionWrapper
import ru.topbun.data.source.remote.api.recipe.RecipeApi
import ru.topbun.data.source.remote.dto.recipe.getRecipe.toRequest
import ru.topbun.data.withInternetCheck
import ru.topbun.domain.entity.recipe.RecipeEntity
import ru.topbun.domain.entity.recipe.addRecipe.AddRecipeEntity
import ru.topbun.domain.entity.recipe.getRecipe.GetRecipeEntity
import ru.topbun.domain.repository.recipe.RecipeRepository

class RecipeRepositoryImpl(
    private val context: Context,
    private val api: RecipeApi
): RecipeRepository {

    override suspend fun getRecipe(data: GetRecipeEntity): Result<List<RecipeEntity>, DataError> =
        context.exceptionWrapper {
            val response = api.getRecipes(data.toRequest())
            val recipes = response.body()
            if (response.isSuccessful && recipes != null){
                Result.Success(recipes.toEntityList())
            } else {
                val error = when(response.code()){
                    HttpStatusCode.BAD_REQUEST -> DataError.Network.INVALID_DATA
                    else -> DataError.Network.SERVER_ERROR
                }
                Result.Error(error)
            }
        }

    override suspend fun getRecipeById(id: Int): Result<RecipeEntity, DataError> =
        context.exceptionWrapper {
            val response = api.getRecipeById(id)
            val recipe = response.body()
            if (response.isSuccessful && recipe != null){
                Result.Success(recipe.toEntity())
            } else {
                val error = when(response.code()){
                    HttpStatusCode.BAD_REQUEST -> DataError.Network.INVALID_DATA
                    HttpStatusCode.NOT_FOUND -> DataError.Network.NOT_FOUND
                    else -> DataError.Network.SERVER_ERROR
                }
                Result.Error(error)
            }
        }

    override suspend fun addRecipe(data: AddRecipeEntity): Result<RecipeEntity, DataError> {
        TODO("Not yet implemented")
    }

    override suspend fun deleteRecipe(id: Int): Result<Unit, DataError> {
        TODO("Not yet implemented")
    }

}