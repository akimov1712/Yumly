package ru.topbun.domain.useCases.recipe

import ru.topbun.domain.entity.recipe.getRecipe.GetRecipeEntity
import ru.topbun.domain.repository.recipe.RecipeRepository

class GetRecipeByUserIdUseCase(
    private val repository: RecipeRepository
) {

    suspend operator fun invoke(userId: Int) = repository.getRecipeByUserId(userId)

}