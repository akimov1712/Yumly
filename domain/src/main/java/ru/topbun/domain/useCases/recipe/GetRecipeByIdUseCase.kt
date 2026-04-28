package ru.topbun.domain.useCases.recipe

import ru.topbun.domain.repository.recipe.RecipeRepository

class GetRecipeByIdUseCase(
    private val repository: RecipeRepository
) {

    suspend operator fun invoke(id: Int, fromCache: Boolean) = repository.getRecipeById(id, fromCache)

}