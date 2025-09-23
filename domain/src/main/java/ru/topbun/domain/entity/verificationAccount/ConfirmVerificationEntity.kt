package ru.topbun.domain.entity.verificationAccount

data class ConfirmVerificationEntity(
    val email: String,
    val code: String
)
