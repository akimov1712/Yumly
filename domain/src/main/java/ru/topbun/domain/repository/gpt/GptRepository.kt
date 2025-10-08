package ru.topbun.domain.repository.gpt

import ru.topbun.common.DataError
import ru.topbun.common.Result
import ru.topbun.domain.entity.gpt.GptChatEntity
import ru.topbun.domain.entity.gpt.SendMessageEntity

interface GptRepository {

    suspend fun getChats(limit: Int, offset: Int): Result<List<GptChatEntity>, DataError>
    suspend fun getChatById(id: Int): Result<GptChatEntity, DataError>
    suspend fun sendMessage(data: SendMessageEntity): Result<GptChatEntity, DataError>

}