package ru.topbun.data.source.local.database.favorite

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import ru.topbun.data.source.local.database.favorite.dbo.FavoriteRecipeDbo

@Dao
interface FavoriteRecipeDao {

    @Query("SELECT * FROM favorite_recipes WHERE userId = :userId ORDER BY position ASC")
    suspend fun getByUser(userId: Int): List<FavoriteRecipeDbo>

    @Query("DELETE FROM favorite_recipes WHERE userId = :userId")
    suspend fun deleteByUser(userId: Int)

    @Query("DELETE FROM favorite_recipes WHERE userId = :userId AND recipeId = :recipeId")
    suspend fun deleteByUserAndRecipe(userId: Int, recipeId: Int)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(items: List<FavoriteRecipeDbo>)

}
