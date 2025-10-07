package ru.topbun.domain.useCases.favorite

import ru.topbun.domain.repository.favorite.FavoriteRepository

class SwitchFavoriteRecipeUseCase(
    private val repository: FavoriteRepository
) {

    suspend operator fun invoke(id: Int) = repository.switchFavoriteRecipe(id)

}