package ru.topbun.data.source.remote.dto.gpt.chat

import ru.topbun.data.source.remote.dto.gpt.message.GptMessageDto
import ru.topbun.domain.entity.gpt.GptChatEntity
import java.util.Date

internal data class GptChatDto (
    val id: Int,
    val userId: Int,
    val messages: List<GptMessageDto>,
    val createdAt: Date,
)