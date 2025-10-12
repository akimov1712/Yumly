package ru.topbun.domain.entity.account

data class ResetPasswordEntity(
    val email: String,
    val password: String,
    val confirmPassword: String
)
