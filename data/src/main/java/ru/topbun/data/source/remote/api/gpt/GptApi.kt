package ru.topbun.data.source.remote.api.gpt

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST
import ru.topbun.data.source.remote.dto.gpt.chat.GetGptChatsRequest
import ru.topbun.data.source.remote.dto.gpt.chat.GetGptChatsResponse

internal interface GptApi {

    @POST("/v1/gpt")
    suspend fun getChats(@Body body: GetGptChatsRequest): Response<List<GetGptChatsResponse>>

}