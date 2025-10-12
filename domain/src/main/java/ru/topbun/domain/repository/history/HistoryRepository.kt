package ru.topbun.domain.repository.history

import ru.topbun.common.error.DataError
import ru.topbun.common.Result

interface HistoryRepository {

    suspend fun getTopQueries(): Result<List<String>, DataError>
    suspend fun getHistory(): Result<List<String>, DataError>

}