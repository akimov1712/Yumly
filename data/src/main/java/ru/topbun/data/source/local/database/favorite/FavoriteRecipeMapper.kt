package ru.topbun.data.source.local.database.favorite

import com.google.gson.Gson
import ru.topbun.data.source.local.database.favorite.dbo.FavoriteRecipeDbo
import ru.topbun.domain.entity.recipe.RecipeEntity

internal object FavoriteRecipeMapper {

    private val gson = Gson()

    fun toDbo(userId: Int, position: Int, recipe: RecipeEntity): FavoriteRecipeDbo =
        FavoriteRecipeDbo(
            userId = userId,
            recipeId = recipe.id,
            position = position,
            payload = gson.toJson(recipe.copy(isFavorite = true))
        )

    fun toEntity(dbo: FavoriteRecipeDbo): RecipeEntity =
        gson.fromJson(dbo.payload, RecipeEntity::class.java)

}
