package ru.topbun.domain.useCases.recipe

import ru.topbun.domain.entity.recipe.addRecipe.AddRecipeEntity
import ru.topbun.domain.repository.recipe.RecipeRepository

class DeleteRecipeUseCase(
    private val repository: RecipeRepository
) {

    suspend operator fun invoke(id: Int) = repository.deleteRecipe(id)

}