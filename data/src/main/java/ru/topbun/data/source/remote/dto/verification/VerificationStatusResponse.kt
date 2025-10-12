package ru.topbun.data.source.remote.dto.verification

import ru.topbun.domain.entity.verification.VerificationStatusType

data class VerificationStatusResponse(
    val status: VerificationStatusType
)
