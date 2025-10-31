package ru.topbun.domain.repository.verification

import ru.topbun.core.common.error.DataError
import ru.topbun.core.common.Result
import ru.topbun.domain.entity.verification.ConfirmVerificationEntity
import ru.topbun.domain.entity.verification.RequestVerificationEntity
import ru.topbun.domain.entity.verification.VerificationStatusType

interface VerificationRepository {

    suspend fun request(request: RequestVerificationEntity): Result<Unit, DataError>
    suspend fun confirm(confirm: ConfirmVerificationEntity): Result<VerificationStatusType, DataError>

}