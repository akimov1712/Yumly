package ru.topbun.domain.entity.gpt

import java.util.Date

data class GptChatEntity(
    val id: Int,
    val userId: Int,
    val messages: List<GptMessageEntity>,
    val maxLimitMessages: Int,
    val createdAt: Date,
)
