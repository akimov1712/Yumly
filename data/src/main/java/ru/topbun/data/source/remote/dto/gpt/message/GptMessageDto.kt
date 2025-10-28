package ru.topbun.data.source.remote.dto.gpt.message

import ru.topbun.domain.entity.gpt.GptMessageEntity
import ru.topbun.domain.entity.gpt.GptMessageRoleType
import java.util.Date

internal data class GptMessageDto(
    val id: Int,
    val role: GptMessageRoleType,
    val text: String,
    val createdAt: Date
){

    fun toEntity() = GptMessageEntity(
        id = id,
        role = role,
        text = text,
        createdAt = createdAt,
    )

}