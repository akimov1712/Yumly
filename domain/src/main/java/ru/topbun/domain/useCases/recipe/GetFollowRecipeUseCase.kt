package ru.topbun.domain.useCases.recipe

import ru.topbun.domain.entity.recipe.getRecipe.GetRecipeEntity
import ru.topbun.domain.repository.recipe.RecipeRepository

class GetFollowRecipeUseCase(
    private val repository: RecipeRepository
) {

    suspend operator fun invoke(data: GetRecipeEntity) = repository.getFollowRecipe(data)

}