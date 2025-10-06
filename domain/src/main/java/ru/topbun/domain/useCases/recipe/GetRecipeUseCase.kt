package ru.topbun.domain.useCases.recipe

import ru.topbun.domain.entity.recipe.getRecipe.GetRecipeEntity
import ru.topbun.domain.repository.recipe.RecipeRepository

class GetRecipeUseCase(
    private val repository: RecipeRepository
) {

    suspend operator fun invoke(data: GetRecipeEntity) = repository.getRecipe(data)

}