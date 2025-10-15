package ru.topbun.data.repository.signUp

import android.content.Context
import ru.topbun.common.HttpStatusCode
import ru.topbun.common.Result
import ru.topbun.common.error.DataError
import ru.topbun.data.exceptionWrapper
import ru.topbun.data.source.remote.api.signUp.SignUpApi
import ru.topbun.data.source.remote.dto.signUp.toRequest
import ru.topbun.data.withInternetCheck
import ru.topbun.domain.entity.account.UserEntity
import ru.topbun.domain.entity.signUp.SignUpEntity
import ru.topbun.domain.repository.signUp.SignUpRepository

class SignUpRepositoryImpl(
    private val context: Context,
    private val api: SignUpApi
) : SignUpRepository {


    override suspend fun signUp(signUp: SignUpEntity): Result<UserEntity, DataError> =
        exceptionWrapper {
            withInternetCheck(context) {
                val response = api.signUp(signUp.toRequest())
                val user = response.body()

                if (response.isSuccessful && user != null) {
                    Result.Success(user.toEntity())
                } else {
                    val error = when (response.code()) {
                        HttpStatusCode.BAD_REQUEST -> DataError.Network.INVALID_DATA
                        HttpStatusCode.CONFLICT -> DataError.Network.EXISTS
                        else -> DataError.Network.SERVER_ERROR
                    }
                    Result.Error(error)
                }
            }
        }


}