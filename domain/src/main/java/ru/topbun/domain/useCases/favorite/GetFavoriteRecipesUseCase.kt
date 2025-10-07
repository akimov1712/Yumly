package ru.topbun.domain.useCases.favorite

import ru.topbun.domain.repository.favorite.FavoriteRepository

class GetFavoriteRecipesUseCase(
    private val repository: FavoriteRepository
) {

    suspend operator fun invoke(userId: Int, limit: Int = 20, offset: Int = 0) =
        repository.getFavoriteRecipes(userId, limit, offset)

}