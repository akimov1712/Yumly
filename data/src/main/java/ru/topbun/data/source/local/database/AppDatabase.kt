package ru.topbun.data.source.local.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [],
    version = 1
)
abstract class AppDatabase : RoomDatabase() {

    companion object{

        private const val DB_NAME = "yumly.db"
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context) = INSTANCE ?: synchronized(Unit){
            INSTANCE ?: createDatabase(context).also { INSTANCE = it }
        }

        fun createDatabase(context: Context) =
            Room.databaseBuilder(context, AppDatabase::class.java, DB_NAME).build()


    }

}