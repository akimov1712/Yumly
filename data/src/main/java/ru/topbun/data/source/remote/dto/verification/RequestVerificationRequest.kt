package ru.topbun.data.source.remote.dto.verification

import ru.topbun.domain.entity.verification.RequestVerificationEntity

data class RequestVerificationRequest(
    val email: String,
    val type: String
)

fun RequestVerificationEntity.toRequest() = RequestVerificationRequest(email, type.toString())