package ru.topbun.data.source.remote.dto.account

import ru.topbun.domain.entity.account.UserEntity
import java.time.LocalDateTime

internal data class UserDto(
    val id: Int,
    val username: String,
    val email: String,
    val password: String,
    val photoUrl: String?,
    val isVerified: Boolean,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime
) {

    fun toEntity() = UserEntity(
        id = id,
        username = username,
        email = email,
        photoUrl = photoUrl,
        isVerified = isVerified,
        createdAt = createdAt,
        updatedAt = updatedAt,
    )

}