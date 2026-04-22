package ru.topbun.domain.entity.gpt

import java.time.LocalDateTime
import java.util.Date

data class GptMessageEntity(
    val id: Int,
    val role: GptMessageRoleType,
    val text: String,
    val createdAt: LocalDateTime
)