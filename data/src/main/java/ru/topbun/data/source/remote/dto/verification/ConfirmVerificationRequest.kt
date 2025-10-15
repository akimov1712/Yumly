package ru.topbun.data.source.remote.dto.verification

import ru.topbun.domain.entity.verification.ConfirmVerificationEntity

data class ConfirmVerificationRequest(
    val email: String,
    val code: String,
    val type: String
)

internal fun ConfirmVerificationEntity.toRequest() = ConfirmVerificationRequest(email, code, type.toString())