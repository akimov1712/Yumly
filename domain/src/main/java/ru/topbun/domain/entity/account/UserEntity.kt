package ru.topbun.domain.entity.account

import java.time.LocalDateTime

data class UserEntity(
    val id: Int,
    val username: String,
    val email: String,
    val photoUrl: String?,
    val isVerified: Boolean,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime
)
