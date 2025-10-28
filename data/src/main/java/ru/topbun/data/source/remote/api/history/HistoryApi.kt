package ru.topbun.data.source.remote.api.history

import retrofit2.Response
import retrofit2.http.GET
import ru.topbun.data.source.remote.dto.history.GetTopQueriesResponse

interface HistoryApi {

    @GET("/v1/history/top")
    suspend fun getTopQueries(): Response<GetTopQueriesResponse>

}