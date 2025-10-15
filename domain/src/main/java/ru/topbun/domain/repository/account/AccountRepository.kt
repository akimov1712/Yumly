package ru.topbun.domain.repository.account

import ru.topbun.common.error.DataError
import ru.topbun.common.Result
import ru.topbun.domain.entity.account.UserEntity
import ru.topbun.domain.entity.account.ProfileEntity
import ru.topbun.domain.entity.account.ResetPasswordEntity
import ru.topbun.domain.entity.account.UpdateAccountInfoEntity

interface AccountRepository {

    suspend fun resetPassword(resetPassword: ResetPasswordEntity): Result<Unit, DataError>
    suspend fun getAccountInfo(): Result<UserEntity, DataError>
    suspend fun updateAccountInfo(data: UpdateAccountInfoEntity): Result<UserEntity, DataError>
    suspend fun getProfile(userId: Int): Result<ProfileEntity, DataError>

}