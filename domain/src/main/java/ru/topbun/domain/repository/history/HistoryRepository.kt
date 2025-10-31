package ru.topbun.domain.repository.history

import ru.topbun.core.common.error.DataError
import ru.topbun.core.common.Result

interface HistoryRepository {

    suspend fun getTopQueries(): Result<List<String>, DataError>
    suspend fun getHistory(): Result<List<String>, DataError>

}