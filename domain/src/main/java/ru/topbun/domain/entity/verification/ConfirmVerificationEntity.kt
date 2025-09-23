package ru.topbun.domain.entity.verification

data class ConfirmVerificationEntity(
    val email: String,
    val code: String,
    val type: VerificationType
)
