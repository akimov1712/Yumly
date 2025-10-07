package ru.topbun.domain.repository.favorite

import ru.topbun.common.DataError
import ru.topbun.common.Result
import ru.topbun.domain.entity.recipe.RecipeEntity

interface FavoriteRepository {

    suspend fun switchFavoriteRecipe(id: Int): Result<Boolean, DataError>
    suspend fun getFavoriteRecipes(userId: Int): Result<List<RecipeEntity>, DataError>

}