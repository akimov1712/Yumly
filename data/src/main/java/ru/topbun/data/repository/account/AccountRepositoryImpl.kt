package ru.topbun.data.repository.account

import android.content.Context
import ru.topbun.common.HttpStatusCode
import ru.topbun.common.Result
import ru.topbun.common.error.DataError
import ru.topbun.data.exceptionWrapper
import ru.topbun.data.source.remote.api.AccountApi
import ru.topbun.data.source.remote.dto.account.toRequest
import ru.topbun.data.withInternetCheck
import ru.topbun.domain.entity.account.ProfileEntity
import ru.topbun.domain.entity.account.ResetPasswordEntity
import ru.topbun.domain.entity.account.UpdateAccountInfoEntity
import ru.topbun.domain.entity.account.UserEntity
import ru.topbun.domain.repository.account.AccountRepository

class AccountRepositoryImpl(
    private val context: Context,
    private val api: AccountApi,
): AccountRepository {

    override suspend fun resetPassword(resetPassword: ResetPasswordEntity): Result<Unit, DataError> =
        exceptionWrapper {
            withInternetCheck(context){
                val response = api.resetPassword(resetPassword.toRequest())
                if (response.isSuccessful){
                    Result.Success(Unit)
                }else{
                    val error = when(response.code){
                        HttpStatusCode.NOT_FOUND -> DataError.Network.NOT_FOUND
                        else -> DataError.Network.SERVER_ERROR
                    }
                    Result.Error(error)
                }
            }
        }

    override suspend fun getAccountInfo(): Result<UserEntity, DataError> =
        exceptionWrapper {
            withInternetCheck(context){
                val response = api.getAccountInfo()
                val user = response.body()
                if (response.isSuccessful && user != null){
                    Result.Success(user.toEntity())
                }else{
                    val error = when(response.code()){
                        HttpStatusCode.NOT_FOUND -> DataError.Network.NOT_FOUND
                        HttpStatusCode.UNAUTHORIZED -> DataError.Network.UNAUTHORIZED
                        else -> DataError.Network.SERVER_ERROR
                    }
                    Result.Error(error)
                }
            }
        }

    override suspend fun updateAccountInfo(data: UpdateAccountInfoEntity): Result<UserEntity, DataError> =
        exceptionWrapper {
            withInternetCheck(context){
                val response = api.updateAccountInfo(data.toRequest())
                val user = response.body()
                if (response.isSuccessful && user != null){
                    Result.Success(user.toEntity())
                }else{
                    val error = when(response.code()){
                        HttpStatusCode.BAD_REQUEST -> DataError.Network.INVALID_DATA
                        HttpStatusCode.NOT_FOUND -> DataError.Network.NOT_FOUND
                        HttpStatusCode.UNAUTHORIZED -> DataError.Network.UNAUTHORIZED
                        else -> DataError.Network.SERVER_ERROR
                    }
                    Result.Error(error)
                }
            }
        }


    override suspend fun getProfile(userId: Int): Result<ProfileEntity, DataError> =
        exceptionWrapper {
            withInternetCheck(context){
                val response = api.getProfile(userId)
                val profile = response.body()
                if (response.isSuccessful && profile != null){
                    Result.Success(profile.toEntity())
                }else{
                    val error = when(response.code()){
                        HttpStatusCode.BAD_REQUEST -> DataError.Network.INVALID_DATA
                        HttpStatusCode.NOT_FOUND -> DataError.Network.NOT_FOUND
                        HttpStatusCode.UNAUTHORIZED -> DataError.Network.UNAUTHORIZED
                        else -> DataError.Network.SERVER_ERROR
                    }
                    Result.Error(error)
                }
            }
        }
}