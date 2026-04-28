package ru.topbun.domain.useCases.favorite

import ru.topbun.domain.repository.favorite.FavoriteRepository

class GetCachedFavoriteRecipesUseCase(
    private val repository: FavoriteRepository
) {

    suspend operator fun invoke(userId: Int) = repository.getCachedFavoriteRecipes(userId)

}
