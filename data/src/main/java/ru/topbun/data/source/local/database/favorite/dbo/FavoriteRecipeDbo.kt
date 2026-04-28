package ru.topbun.data.source.local.database.favorite.dbo

import androidx.room.Entity

@Entity(tableName = "favorite_recipes", primaryKeys = ["userId", "recipeId"])
data class FavoriteRecipeDbo(
    val userId: Int,
    val recipeId: Int,
    val position: Int,
    val payload: String,
)
