package ru.topbun.domain.repository.login

import ru.topbun.core.common.error.DataError
import ru.topbun.core.common.Result
import ru.topbun.domain.entity.login.LoginEntity

interface LoginRepository {

    suspend fun login(login: LoginEntity): Result<Unit, DataError>

}