package ru.topbun.data.source.local.database.history.dbo

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity("histories")
data class HistoryDbo (
    @PrimaryKey(true)
    val id: Int = 0,
    val query: String
)