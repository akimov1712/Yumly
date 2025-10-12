package ru.topbun.data.repository.login

import android.content.Context
import android.content.SharedPreferences
import ru.topbun.common.HttpStatusCode
import ru.topbun.common.Result
import ru.topbun.common.error.DataError
import ru.topbun.data.exceptionWrapper
import ru.topbun.data.source.local.config.saveToken
import ru.topbun.data.source.remote.api.LoginApi
import ru.topbun.data.source.remote.dto.login.toRequest
import ru.topbun.data.withInternetCheck
import ru.topbun.domain.entity.login.LoginEntity
import ru.topbun.domain.repository.login.LoginRepository

class LoginRepositoryImpl(
    private val context: Context,
    private val api: LoginApi,
    private val cryptConfig: SharedPreferences
) : LoginRepository {

    override suspend fun login(login: LoginEntity): Result<Unit, DataError> =
        exceptionWrapper {
            withInternetCheck(context) {
                val response = api.login(login.toRequest())
                val token = response.body()

                if (response.isSuccessful && token != null) {
                    cryptConfig.saveToken(token.token)
                    Result.Success(Unit)
                } else {
                    val error = when (response.code()) {
                        HttpStatusCode.NotFound -> DataError.Network.SERVER_ERROR
                        else -> DataError.Network.SERVER_ERROR
                    }
                    Result.Error(error)
                }
            }
        }

}