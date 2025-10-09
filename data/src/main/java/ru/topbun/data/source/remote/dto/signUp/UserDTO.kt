package ru.topbun.data.source.remote.dto.signUp

import java.util.Date

data class UserDTO(
    val id: Int,
    val username: String,
    val email: String,
    val password: String,
    val photoUrl: String?,
    val isVerified: Boolean,
    val createdAt: Date,
    val updatedAt: Date
)