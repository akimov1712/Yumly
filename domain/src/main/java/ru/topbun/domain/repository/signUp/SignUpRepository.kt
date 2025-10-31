package ru.topbun.domain.repository.signUp

import ru.topbun.core.common.error.DataError
import ru.topbun.core.common.Result
import ru.topbun.domain.entity.account.UserEntity
import ru.topbun.domain.entity.signUp.SignUpEntity

interface SignUpRepository {

    suspend fun signUp(signUp: SignUpEntity): Result<UserEntity, DataError>

}