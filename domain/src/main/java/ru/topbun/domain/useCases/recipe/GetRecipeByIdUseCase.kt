package ru.topbun.domain.useCases.recipe

import ru.topbun.domain.entity.recipe.getRecipe.GetRecipeEntity
import ru.topbun.domain.repository.recipe.RecipeRepository

class GetRecipeByIdUseCase(
    private val repository: RecipeRepository
) {

    suspend operator fun invoke(id: Int) = repository.getRecipeById(id)

}