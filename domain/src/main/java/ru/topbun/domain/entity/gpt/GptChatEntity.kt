package ru.topbun.domain.entity.gpt

import java.time.LocalDateTime

data class GptChatEntity(
    val id: Int,
    val userId: Int,
    val messages: List<GptMessageEntity>,
    val maxLimitMessages: Int,
    val createdAt: LocalDateTime,
)
