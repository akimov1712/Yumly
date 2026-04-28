package ru.topbun.data.repository.recipe

import android.content.Context
import ru.topbun.core.common.HttpStatusCode
import ru.topbun.core.common.Result
import ru.topbun.core.common.error.DataError
import ru.topbun.data.exceptionWrapper
import ru.topbun.data.source.local.database.favorite.FavoriteRecipeDao
import ru.topbun.data.source.local.database.favorite.FavoriteRecipeMapper
import ru.topbun.data.source.local.database.history.HistoryDao
import ru.topbun.data.source.local.database.history.dbo.HistoryDbo
import ru.topbun.data.source.remote.api.recipe.RecipeApi
import ru.topbun.data.source.remote.dto.recipe.addRecipe.toRequest
import ru.topbun.data.source.remote.dto.recipe.getRecipe.toRequest
import ru.topbun.domain.entity.recipe.RecipeEntity
import ru.topbun.domain.entity.recipe.addRecipe.AddRecipeEntity
import ru.topbun.domain.entity.recipe.getRecipe.GetRecipeByUserIdEntity
import ru.topbun.domain.entity.recipe.getRecipe.GetRecipeEntity
import ru.topbun.domain.entity.recipe.tag.TagRecipeEntity
import ru.topbun.domain.repository.recipe.RecipeRepository

internal class RecipeRepositoryImpl(
    private val context: Context,
    private val api: RecipeApi,
    private val historyDao: HistoryDao,
    private val favoriteDao: FavoriteRecipeDao
) : RecipeRepository {

    override suspend fun getRecipe(data: GetRecipeEntity): Result<List<RecipeEntity>, DataError> =
        context.exceptionWrapper {
            val response = api.getRecipes(data.toRequest())
            val recipes = response.body()
            if (response.isSuccessful && recipes != null) {
                insertHistoryQuery(data.q, data.offset)
                Result.Success(recipes.toEntityList())
            } else {
                val error = when (response.code()) {
                    HttpStatusCode.BAD_REQUEST -> DataError.Network.INVALID_DATA
                    else -> DataError.Network.SERVER_ERROR
                }
                Result.Error(error)
            }
        }

    override suspend fun getFollowRecipe(data: GetRecipeEntity): Result<List<RecipeEntity>, DataError> =
        context.exceptionWrapper {
            val response = api.getFollowRecipes(data.toRequest())
            val recipes = response.body()
            if (response.isSuccessful && recipes != null) {
                insertHistoryQuery(data.q, data.offset)
                Result.Success(recipes.toEntityList())
            } else {
                val error = when (response.code()) {
                    HttpStatusCode.BAD_REQUEST -> DataError.Network.INVALID_DATA
                    else -> DataError.Network.SERVER_ERROR
                }
                Result.Error(error)
            }
        }

    private suspend fun insertHistoryQuery(q: String?, offset: Int) {
        if (!q.isNullOrBlank() && offset == 0) {
            val dbo = HistoryDbo(query = q)
            historyDao.addHistory(dbo)
        }
    }

    override suspend fun getRecipeById(
        id: Int,
        fromCache: Boolean
    ): Result<RecipeEntity, DataError> {
        return if (fromCache) {
            try {
                val recipe = favoriteDao.selectById(id)
                Result.Success(FavoriteRecipeMapper.toEntity(recipe))
            } catch (e: Exception){
                Result.Error(DataError.Network.NO_INTERNET)
            }
        } else {
            context.exceptionWrapper {

                val response = api.getRecipeById(id)
                val recipe = response.body()
                if (response.isSuccessful && recipe != null) {
                    Result.Success(recipe.toEntity())
                } else {
                    val error = when (response.code()) {
                        HttpStatusCode.BAD_REQUEST -> DataError.Network.INVALID_DATA
                        HttpStatusCode.NOT_FOUND -> DataError.Network.NOT_FOUND
                        else -> DataError.Network.SERVER_ERROR
                    }
                    Result.Error(error)
                }
            }
        }
    }


    override suspend fun getRecipeByUserId(
        userId: Int,
        data: GetRecipeByUserIdEntity
    ): Result<List<RecipeEntity>, DataError> =
        context.exceptionWrapper {
            val response = api.getRecipeByUserId(userId, data.toRequest())
            val recipes = response.body()
            if (response.isSuccessful && recipes != null) {
                Result.Success(recipes.toEntityList())
            } else {
                val error = when (response.code()) {
                    HttpStatusCode.BAD_REQUEST -> DataError.Network.INVALID_DATA
                    else -> DataError.Network.SERVER_ERROR
                }
                Result.Error(error)
            }
        }

    override suspend fun addRecipe(data: AddRecipeEntity): Result<RecipeEntity, DataError> =
        context.exceptionWrapper {
            val response = api.addRecipe(data.toRequest())
            val recipe = response.body()
            if (response.isSuccessful && recipe != null) {
                Result.Success(recipe.toEntity())
            } else {
                val error = when (response.code()) {
                    HttpStatusCode.CONFLICT -> DataError.Network.INVALID_DATA
                    HttpStatusCode.UNAUTHORIZED -> DataError.Network.UNAUTHORIZED
                    else -> DataError.Network.SERVER_ERROR
                }
                Result.Error(error)
            }
        }

    override suspend fun deleteRecipe(id: Int): Result<Unit, DataError> =
        context.exceptionWrapper {
            val response = api.deleteRecipe(id)
            if (response.isSuccessful) {
                Result.Success(Unit)
            } else {
                val error = when (response.code()) {
                    HttpStatusCode.BAD_REQUEST -> DataError.Network.INVALID_DATA
                    HttpStatusCode.UNAUTHORIZED -> DataError.Network.UNAUTHORIZED
                    HttpStatusCode.FORBIDDEN -> DataError.Network.UNAUTHORIZED
                    HttpStatusCode.NOT_FOUND -> DataError.Network.NOT_FOUND
                    else -> DataError.Network.SERVER_ERROR
                }
                Result.Error(error)
            }
        }

    override suspend fun getTags(): Result<List<TagRecipeEntity>, DataError> =
        context.exceptionWrapper {
            val response = api.getTags()
            val tags = response.body()
            if (response.isSuccessful && tags != null) {
                Result.Success(tags.map { it.toEntity() })
            } else {
                Result.Error(DataError.Network.SERVER_ERROR)
            }
        }

}
