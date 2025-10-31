package ru.topbun.data.repository.login

import android.content.Context
import ru.topbun.core.common.HttpStatusCode
import ru.topbun.core.common.Result
import ru.topbun.core.common.error.DataError
import ru.topbun.data.exceptionWrapper
import ru.topbun.data.source.local.config.TokenManager
import ru.topbun.data.source.remote.api.login.LoginApi
import ru.topbun.data.source.remote.dto.login.toRequest
import ru.topbun.domain.entity.login.LoginEntity
import ru.topbun.domain.repository.login.LoginRepository

internal class LoginRepositoryImpl(
    private val context: Context,
    private val api: LoginApi,
    private val tokenManager: TokenManager
) : LoginRepository {

    override suspend fun login(login: LoginEntity): Result<Unit, DataError> =
        context.exceptionWrapper {
            val response = api.login(login.toRequest())
            val token = response.body()

            if (response.isSuccessful && token != null) {
                tokenManager.saveToken(token.token)
                Result.Success(Unit)
            } else {
                val error = when (response.code()) {
                    HttpStatusCode.NOT_FOUND -> DataError.Network.SERVER_ERROR
                    else -> DataError.Network.SERVER_ERROR
                }
                Result.Error(error)
            }
        }

}