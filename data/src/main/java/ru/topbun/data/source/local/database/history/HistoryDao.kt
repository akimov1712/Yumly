package ru.topbun.data.source.local.database.history

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import ru.topbun.data.source.local.database.history.dbo.HistoryDbo

@Dao
interface HistoryDao {

    @Query("SELECT * FROM histories")
    suspend fun getHistory(): List<HistoryDbo>

    @Insert
    suspend fun addHistory(data: HistoryDbo)
}
