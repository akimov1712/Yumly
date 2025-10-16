package ru.topbun.data.source.remote.dto.verification

import ru.topbun.domain.entity.verification.RequestVerificationEntity

internal data class RequestVerificationRequest(
    val email: String,
    val type: String
)

internal fun RequestVerificationEntity.toRequest() = RequestVerificationRequest(email, type.toString())