package ru.topbun.data.repository.login

import android.content.Context
import ru.topbun.core.common.HttpStatusCode
import ru.topbun.core.common.Result
import ru.topbun.core.common.error.DataError
import ru.topbun.data.exceptionWrapper
import ru.topbun.data.source.local.config.TokenManager
import ru.topbun.data.source.remote.api.login.LoginApi
import ru.topbun.data.source.remote.dto.login.SuccessLoginResponse
import ru.topbun.data.source.remote.dto.login.UnauthorizedLoginResponse
import ru.topbun.data.source.remote.dto.login.toRequest
import ru.topbun.domain.entity.login.LoginEntity
import ru.topbun.domain.repository.login.LoginRepository

internal class LoginRepositoryImpl(
    private val context: Context,
    private val api: LoginApi,
    private val tokenManager: TokenManager
) : LoginRepository {

    override suspend fun login(login: LoginEntity): Result<String, DataError> =
        context.exceptionWrapper {
            val response = api.login(login.toRequest())
            val result = response.body()

            if (response.isSuccessful && result != null) {
                when(result){
                    is SuccessLoginResponse -> {
                        tokenManager.saveToken(result.token)
                        Result.Success(result.token)
                    }
                    is UnauthorizedLoginResponse -> {
                        val error = DataError.Network.NOT_VERIFIED
                        Result.Error(error, result.email)
                    }
                }

            } else {
                val error = when (response.code()) {
                    HttpStatusCode.NOT_FOUND -> DataError.Network.NOT_FOUND
                    else -> DataError.Network.SERVER_ERROR
                }
                Result.Error(error)
            }
        }

}