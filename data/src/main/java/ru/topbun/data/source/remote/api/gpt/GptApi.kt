package ru.topbun.data.source.remote.api.gpt

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import ru.topbun.data.source.remote.dto.gpt.chat.GetGptChatsRequest
import ru.topbun.data.source.remote.dto.gpt.chat.GetGptChatsResponse
import ru.topbun.data.source.remote.dto.gpt.chat.GptChatResponse
import ru.topbun.data.source.remote.dto.gpt.message.SendMessageRequest

internal interface GptApi {

    @POST("/v1/gpt")
    suspend fun getChats(@Body body: GetGptChatsRequest): Response<GetGptChatsResponse>

    @GET("/v1/gpt/{id}")
    suspend fun getChatById(@Path("id") id: Int): Response<GptChatResponse>

    @POST("/v1/gpt/send")
    suspend fun sendMessage(@Body body: SendMessageRequest): Response<GptChatResponse>


}