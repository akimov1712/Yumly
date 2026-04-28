package ru.topbun.navigation

import cafe.adriel.voyager.core.registry.ScreenProvider

sealed interface RecipeScreenProvider : ScreenProvider {

    data class Detail(val recipeId: Int, val fromCache: Boolean = false) : RecipeScreenProvider

}
