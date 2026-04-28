package ru.topbun.data.source.local.database.favorite

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import ru.topbun.data.source.local.database.favorite.dbo.FavoriteRecipeDbo

@Dao
interface FavoriteRecipeDao {

    @Query("SELECT * FROM favorite_recipes ORDER BY position ASC")
    suspend fun selectAll(): List<FavoriteRecipeDbo>

    @Query("DELETE FROM favorite_recipes WHERE userId = :userId")
    suspend fun deleteByUser(userId: Int)

    @Query("DELETE FROM favorite_recipes")
    suspend fun deleteAll()

    @Query("SELECT * FROM favorite_recipes WHERE recipeId = :recipeId")
    suspend fun selectById(recipeId: Int): FavoriteRecipeDbo

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(items: List<FavoriteRecipeDbo>)

}
