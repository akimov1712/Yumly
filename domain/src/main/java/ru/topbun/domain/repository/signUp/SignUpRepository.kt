package ru.topbun.domain.repository.signUp

import ru.topbun.common.DataError
import ru.topbun.common.Result
import ru.topbun.domain.entity.signUp.SignUpEntity

interface SignUpRepository {

    suspend fun signUp(signUp: SignUpEntity): Result<Unit, DataError>

}