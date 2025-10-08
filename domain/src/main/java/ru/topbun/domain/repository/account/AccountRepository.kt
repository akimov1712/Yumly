package ru.topbun.domain.repository.account

import ru.topbun.common.DataError
import ru.topbun.common.Result
import ru.topbun.domain.entity.account.UserEntity
import ru.topbun.domain.entity.account.ProfileEntity
import ru.topbun.domain.entity.account.ResetPasswordEntity

interface AccountRepository {

    suspend fun resetPassword(resetPassword: ResetPasswordEntity): Result<Unit, DataError>
    suspend fun getAccountInfo(): Result<UserEntity, DataError>
    suspend fun updateAccountInfo(username: String?, photoUrl: String?): Result<UserEntity, DataError>
    suspend fun getProfile(userId: Int): Result<ProfileEntity, DataError>

}