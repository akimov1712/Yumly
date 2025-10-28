package ru.topbun.data.source.remote.dto.gpt.message

import ru.topbun.domain.entity.gpt.SendMessageEntity

internal data class SendMessageRequest(
    val chatId: Int?,
    val text: String
)

internal fun SendMessageEntity.toDto() = SendMessageRequest(
    chatId = chatId,
    text = text,
)
