package ru.topbun.data.repository.favorite

import android.content.Context
import ru.topbun.core.common.HttpStatusCode
import ru.topbun.core.common.Result
import ru.topbun.core.common.error.DataError
import ru.topbun.data.exceptionWrapper
import ru.topbun.data.source.local.database.favorite.FavoriteRecipeDao
import ru.topbun.data.source.local.database.favorite.FavoriteRecipeMapper
import ru.topbun.data.source.remote.api.favorite.FavoriteApi
import ru.topbun.data.source.remote.dto.favorite.GetFavoriteRequest
import ru.topbun.domain.entity.recipe.RecipeEntity
import ru.topbun.domain.repository.favorite.FavoriteRepository

internal class FavoriteRepositoryImpl(
    private val context: Context,
    private val api: FavoriteApi,
    private val dao: FavoriteRecipeDao,
): FavoriteRepository {

    override suspend fun switchFavoriteRecipe(id: Int): Result<Boolean, DataError> =
        context.exceptionWrapper {
            val response = api.switchFavorite(id)
            val status = response.body()
            if (response.isSuccessful && status != null){
                Result.Success(status)
            } else {
                val error = when(response.code()){
                    HttpStatusCode.BAD_REQUEST -> DataError.Network.INVALID_DATA
                    HttpStatusCode.NOT_FOUND -> DataError.Network.NOT_FOUND
                    HttpStatusCode.UNAUTHORIZED -> DataError.Network.UNAUTHORIZED
                    else -> DataError.Network.SERVER_ERROR
                }
                Result.Error(error)
            }
        }

    override suspend fun getFavoriteRecipes(userId: Int, limit: Int, offset: Int): Result<List<RecipeEntity>, DataError> =
        context.exceptionWrapper {
            val request = GetFavoriteRequest(limit, offset)
            val response = api.getFavoriteRecipes(userId, request)
            val recipes = response.body()
            if (response.isSuccessful && recipes != null){
                val entities = recipes.toEntityList()
                cacheRecipes(userId, offset, entities)
                Result.Success(entities)
            } else {
                val error = when(response.code()){
                    HttpStatusCode.BAD_REQUEST -> DataError.Network.INVALID_DATA
                    else -> DataError.Network.SERVER_ERROR
                }
                Result.Error(error)
            }
        }

    override suspend fun getCachedFavoriteRecipes(): List<RecipeEntity> =
        runCatching {
            dao.selectAll().map { FavoriteRecipeMapper.toEntity(it) }
        }.getOrElse { emptyList() }

    private suspend fun cacheRecipes(userId: Int, offset: Int, recipes: List<RecipeEntity>) {
        if (recipes.isEmpty() && offset == 0) {
            dao.deleteByUser(userId)
            return
        }
        if (offset == 0) dao.deleteByUser(userId)
        val items = recipes.mapIndexed { index, recipe ->
            FavoriteRecipeMapper.toDbo(userId, offset + index, recipe)
        }
        if (items.isNotEmpty()) dao.insertAll(items)
    }

}
