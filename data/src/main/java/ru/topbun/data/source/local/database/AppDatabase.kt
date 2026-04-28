package ru.topbun.data.source.local.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import ru.topbun.data.source.local.database.favorite.FavoriteRecipeDao
import ru.topbun.data.source.local.database.favorite.dbo.FavoriteRecipeDbo
import ru.topbun.data.source.local.database.history.HistoryDao
import ru.topbun.data.source.local.database.history.dbo.HistoryDbo

@Database(
    entities = [HistoryDbo::class, FavoriteRecipeDbo::class],
    version = 2
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun historyDao(): HistoryDao
    abstract fun favoriteRecipeDao(): FavoriteRecipeDao

    companion object{
        private const val DB_NAME = "yumly.db"
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context) = INSTANCE ?: synchronized(Unit){
            INSTANCE ?: createDatabase(context).also { INSTANCE = it }
        }

        private fun createDatabase(context: Context) =
            Room.databaseBuilder(context, AppDatabase::class.java, DB_NAME)
                .fallbackToDestructiveMigration(true)
                .build()

    }

}
