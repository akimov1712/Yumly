package ru.topbun.domain.repository.verification

import ru.topbun.common.DataError
import ru.topbun.common.Result
import ru.topbun.domain.entity.verification.ConfirmVerificationEntity
import ru.topbun.domain.entity.verification.RequestVerificationEntity
import ru.topbun.domain.entity.verification.VerificationStatusType

interface VerificationRepository {

    suspend fun request(request: RequestVerificationEntity): Result<VerificationStatusType, DataError>
    suspend fun confirm(confirm: ConfirmVerificationEntity): Result<VerificationStatusType, DataError>

}