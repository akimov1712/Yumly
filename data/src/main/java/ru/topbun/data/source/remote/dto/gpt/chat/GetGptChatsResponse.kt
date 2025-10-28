package ru.topbun.data.source.remote.dto.gpt.chat

import ru.topbun.domain.entity.gpt.GptChatEntity

internal data class GetGptChatsResponse(
    val maxLimitMessages: Int,
    val chat: GptChatDto
) {

    fun toEntity() = GptChatEntity(
        id = chat.id,
        userId = chat.userId,
        messages = chat.messages.map { it.toEntity() },
        maxLimitMessages = maxLimitMessages,
        createdAt = chat.createdAt,
    )
}

internal fun List<GetGptChatsResponse>.toEntity() = this.map { it.toEntity() }