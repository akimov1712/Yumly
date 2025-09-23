package ru.topbun.domain.entity.account

data class ResetPasswordEntity(
    val email: String,
    val newPassword: String
)
