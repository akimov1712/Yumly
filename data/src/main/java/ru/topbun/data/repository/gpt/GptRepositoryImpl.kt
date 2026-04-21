package ru.topbun.data.repository.gpt

import android.content.Context
import ru.topbun.core.common.HttpStatusCode
import ru.topbun.core.common.Result
import ru.topbun.core.common.error.DataError
import ru.topbun.data.exceptionWrapper
import ru.topbun.data.source.remote.api.gpt.GptApi
import ru.topbun.data.source.remote.dto.gpt.chat.GetGptChatsRequest
import ru.topbun.data.source.remote.dto.gpt.message.toDto
import ru.topbun.domain.entity.gpt.GptChatEntity
import ru.topbun.domain.entity.gpt.SendMessageEntity
import ru.topbun.domain.repository.gpt.GptRepository

internal class GptRepositoryImpl(
    private val context: Context,
    private val api: GptApi
) : GptRepository {

    override suspend fun getChats(limit: Int, offset: Int): Result<List<GptChatEntity>, DataError> =
        context.exceptionWrapper {
            val request = GetGptChatsRequest(offset = offset, limit = limit)
            val response = api.getChats(request)
            val chats = response.body()
            if (response.isSuccessful && chats != null) {
                Result.Success(chats.toEntity())
            } else {
                val error = when (response.code()) {
                    HttpStatusCode.UNAUTHORIZED -> DataError.Network.UNAUTHORIZED
                    else -> DataError.Network.SERVER_ERROR
                }
                Result.Error(error)
            }
        }

    override suspend fun getChatById(id: Int): Result<GptChatEntity, DataError> =
        context.exceptionWrapper {
            val response = api.getChatById(id)
            val chat = response.body()
            if (response.isSuccessful && chat != null) {
                Result.Success(chat.toEntity())
            } else {
                val error = when (response.code()) {
                    HttpStatusCode.UNAUTHORIZED -> DataError.Network.UNAUTHORIZED
                    HttpStatusCode.BAD_REQUEST -> DataError.Network.INVALID_DATA
                    HttpStatusCode.NOT_FOUND -> DataError.Network.NOT_FOUND
                    HttpStatusCode.FORBIDDEN -> DataError.Network.FORBIDDEN
                    else -> DataError.Network.SERVER_ERROR
                }
                Result.Error(error)
            }
        }

    override suspend fun sendMessage(data: SendMessageEntity): Result<GptChatEntity, DataError> =
        context.exceptionWrapper {
            val response = api.sendMessage(data.toDto())
            val newChat = response.body()
            if (response.isSuccessful && newChat != null) {
                Result.Success(newChat.toEntity())
            } else {
                val error = when (response.code()) {
                    HttpStatusCode.UNAUTHORIZED -> DataError.Network.UNAUTHORIZED
                    HttpStatusCode.BAD_REQUEST -> DataError.Network.BAD_REQUEST
                    HttpStatusCode.NOT_FOUND -> DataError.Network.NOT_FOUND
                    else -> DataError.Network.SERVER_ERROR
                }
                Result.Error(error)
            }
        }

}
