package ru.topbun.data.repository.verification

import android.content.Context
import ru.topbun.common.HttpStatusCode
import ru.topbun.common.Result
import ru.topbun.common.error.DataError
import ru.topbun.data.exceptionWrapper
import ru.topbun.data.source.remote.api.VerificationApi
import ru.topbun.data.source.remote.dto.verification.toRequest
import ru.topbun.data.withInternetCheck
import ru.topbun.domain.entity.verification.ConfirmVerificationEntity
import ru.topbun.domain.entity.verification.RequestVerificationEntity
import ru.topbun.domain.entity.verification.VerificationStatusType
import ru.topbun.domain.repository.verification.VerificationRepository

class VerificationRepositoryImpl(
    private val context: Context,
    private val api: VerificationApi
): VerificationRepository {

    override suspend fun request(data: RequestVerificationEntity): Result<Unit, DataError> =
        exceptionWrapper {
            withInternetCheck(context){
                val response = api.request(data.toRequest())
                if (response.isSuccessful){
                    Result.Success(Unit)
                } else {
                    val error = when(response.code){
                        HttpStatusCode.NotFound -> DataError.Network.NOT_FOUND
                        else -> DataError.Network.SERVER_ERROR
                    }
                    Result.Error(error)
                }
            }
        }

    override suspend fun confirm(confirm: ConfirmVerificationEntity): Result<VerificationStatusType, DataError> {
        TODO("Not yet implemented")
    }

}