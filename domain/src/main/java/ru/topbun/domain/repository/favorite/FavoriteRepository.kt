package ru.topbun.domain.repository.favorite

import ru.topbun.common.error.DataError
import ru.topbun.common.Result
import ru.topbun.domain.entity.recipe.RecipeEntity

interface FavoriteRepository {

    suspend fun switchFavoriteRecipe(id: Int): Result<Boolean, DataError>
    suspend fun getFavoriteRecipes(userId: Int, limit: Int = 20, offset: Int = 0): Result<List<RecipeEntity>, DataError>

}