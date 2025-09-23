package ru.topbun.domain.repository.account

import ru.topbun.common.DataError
import ru.topbun.common.Result
import ru.topbun.domain.entity.account.ResetPasswordEntity

interface AccountRepository {

    suspend fun resetPassword(resetPassword: ResetPasswordEntity): Result<Unit, DataError>

}