package ru.topbun.data.source.remote.dto.gpt.chat

import ru.topbun.data.source.remote.dto.gpt.message.GptMessageDto
import java.time.LocalDateTime
import java.util.Date

internal data class GptChatDto(
    val id: Int,
    val userId: Int,
    val messages: List<GptMessageDto>,
    val createdAt: LocalDateTime,
)