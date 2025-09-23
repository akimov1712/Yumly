package ru.topbun.domain.entity.resetPassword

data class ConfirmResetPasswordEntity(
    val email: String,
    val code: String
)
