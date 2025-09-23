package ru.topbun.domain.entity.verification

data class RequestVerificationEntity(
    val email: String,
    val type: VerificationType
)
