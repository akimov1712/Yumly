package ru.topbun.data.source.remote.dto.verification

import ru.topbun.domain.entity.verification.RequestVerificationEntity
import ru.topbun.domain.entity.verification.VerificationType

data class RequestVerificationRequest(
    val email: String,
    val type: String
)

fun RequestVerificationEntity.toRequest() = RequestVerificationRequest(email, type.toString())