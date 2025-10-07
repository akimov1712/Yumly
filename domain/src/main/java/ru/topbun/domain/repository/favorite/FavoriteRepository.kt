package ru.topbun.domain.repository.favorite

import ru.topbun.common.DataError
import ru.topbun.common.Result

interface FavoriteRepository {

    suspend fun switchFavoriteRecipe(id: Int): Result<Boolean, DataError>

}