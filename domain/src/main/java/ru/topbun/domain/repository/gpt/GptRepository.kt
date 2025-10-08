package ru.topbun.domain.repository.gpt

import ru.topbun.common.DataError
import ru.topbun.common.Result
import ru.topbun.domain.entity.gpt.GptChatEntity

interface GptRepository {

    suspend fun getChats(limit: Int, offset: Int): Result<List<GptChatEntity>, DataError>

}