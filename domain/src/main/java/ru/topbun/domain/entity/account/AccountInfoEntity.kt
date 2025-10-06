package ru.topbun.domain.entity.account

import java.util.Date

data class AccountInfoEntity(
    val id: Int,
    val username: Int,
    val email: String,
    val photoUrl: String?,
    val isVerified: Boolean,
    val createdAt: Date,
    val updatedAt: Date
)
