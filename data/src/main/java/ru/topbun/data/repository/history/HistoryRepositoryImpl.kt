package ru.topbun.data.repository.history

import android.content.Context
import ru.topbun.common.HttpStatusCode
import ru.topbun.common.Result
import ru.topbun.common.error.DataError
import ru.topbun.data.exceptionWrapper
import ru.topbun.data.source.local.database.history.HistoryDao
import ru.topbun.data.source.remote.api.history.HistoryApi
import ru.topbun.domain.repository.history.HistoryRepository

class HistoryRepositoryImpl(
    private val context: Context,
    private val api: HistoryApi,
    private val dao: HistoryDao
): HistoryRepository {

    override suspend fun getTopQueries(): Result<List<String>, DataError> =
        context.exceptionWrapper {
            val response = api.getTopQueries()
            val result = response.body()
            if (response.isSuccessful && result != null){
                Result.Success(result)
            } else {
                val error = when(response.code()){
                    else -> DataError.Network.SERVER_ERROR
                }
                Result.Error(error)
            }
        }

    override suspend fun getHistory(): Result<List<String>, DataError> {
        val history = dao.getHistory()
        return Result.Success(history.map { it.query })
    }
}