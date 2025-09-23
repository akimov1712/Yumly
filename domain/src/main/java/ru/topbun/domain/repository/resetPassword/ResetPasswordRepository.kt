package ru.topbun.domain.repository.resetPassword

import ru.topbun.common.DataError
import ru.topbun.common.Result
import ru.topbun.domain.entity.resetPassword.ConfirmResetPasswordEntity
import ru.topbun.domain.entity.resetPassword.RequestResetPasswordEntity
import ru.topbun.domain.entity.verificationAccount.ConfirmVerificationEntity
import ru.topbun.domain.entity.verificationAccount.RequestVerificationEntity

interface ResetPasswordRepository {

    suspend fun request(request: RequestResetPasswordEntity): Result<Unit, DataError>
    suspend fun confirm(confirm: ConfirmResetPasswordEntity): Result<Unit, DataError>

}