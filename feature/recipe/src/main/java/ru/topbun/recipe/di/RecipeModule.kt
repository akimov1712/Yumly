package ru.topbun.recipe.di

import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module
import ru.topbun.recipe.RecipeViewModel

val recipeModule = module {
    viewModel { (recipeId: Int, fromCache: Boolean) ->
        RecipeViewModel(
            recipeId,
            fromCache,
            get(),
            get(),
            get(),
            get(),
            get(),
        )
    }
}
