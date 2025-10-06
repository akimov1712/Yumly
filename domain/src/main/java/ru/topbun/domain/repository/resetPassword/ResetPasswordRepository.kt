package ru.topbun.domain.repository.resetPassword

import ru.topbun.common.DataError
import ru.topbun.common.Result
import ru.topbun.domain.entity.resetPassword.ConfirmResetPasswordEntity
import ru.topbun.domain.entity.resetPassword.RequestResetPasswordEntity

interface ResetPasswordRepository {

    suspend fun request(request: RequestResetPasswordEntity): Result<Unit, DataError>
    suspend fun confirm(confirm: ConfirmResetPasswordEntity): Result<Unit, DataError>

}