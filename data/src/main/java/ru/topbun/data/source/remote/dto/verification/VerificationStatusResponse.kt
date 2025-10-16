package ru.topbun.data.source.remote.dto.verification

import ru.topbun.domain.entity.verification.VerificationStatusType

internal data class VerificationStatusResponse(
    val status: VerificationStatusType
)
