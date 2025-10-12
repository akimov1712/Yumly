package ru.topbun.data.source.remote.dto.signUp

import ru.topbun.domain.entity.account.UserEntity
import java.util.Date

data class UserDto(
    val id: Int,
    val username: String,
    val email: String,
    val password: String,
    val photoUrl: String?,
    val isVerified: Boolean,
    val createdAt: Date,
    val updatedAt: Date
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