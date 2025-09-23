package ru.topbun.domain.repository.verificationAccount

import ru.topbun.common.DataError
import ru.topbun.common.Result
import ru.topbun.domain.entity.verificationAccount.ConfirmVerificationEntity
import ru.topbun.domain.entity.verificationAccount.RequestVerificationEntity

interface VerificationRepository {

    suspend fun request(request: RequestVerificationEntity): Result<Unit, DataError>
    suspend fun confirm(confirm: ConfirmVerificationEntity): Result<Unit, DataError>

}