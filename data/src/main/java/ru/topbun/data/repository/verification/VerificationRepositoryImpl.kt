package ru.topbun.data.repository.verification

import android.content.Context
import com.google.gson.Gson
import ru.topbun.core.common.HttpStatusCode
import ru.topbun.core.common.Result
import ru.topbun.core.common.error.DataError
import ru.topbun.data.exceptionWrapper
import ru.topbun.data.source.local.config.TokenManager
import ru.topbun.data.source.remote.api.verification.VerificationApi
import ru.topbun.data.source.remote.dto.verification.TokenResponse
import ru.topbun.data.source.remote.dto.verification.VerificationStatusResponse
import ru.topbun.data.source.remote.dto.verification.toRequest
import ru.topbun.domain.entity.verification.ConfirmVerificationEntity
import ru.topbun.domain.entity.verification.RequestVerificationEntity
import ru.topbun.domain.entity.verification.VerificationStatusType
import ru.topbun.domain.entity.verification.VerificationType
import ru.topbun.domain.repository.verification.VerificationRepository

internal class VerificationRepositoryImpl(
    private val context: Context,
    private val api: VerificationApi,
    private val tokenManager: TokenManager,
    private val gson: Gson,
) : VerificationRepository {

    override suspend fun request(data: RequestVerificationEntity): Result<Unit, DataError> =
        context.exceptionWrapper {
            val response = api.request(data.toRequest())
            if (response.isSuccessful) {
                Result.Success(Unit)
            } else {
                val error = when (response.code()) {
                    HttpStatusCode.NOT_FOUND -> DataError.Network.NOT_FOUND
                    else -> DataError.Network.SERVER_ERROR
                }
                Result.Error(error)
            }
        }

    override suspend fun confirm(data: ConfirmVerificationEntity): Result<VerificationStatusType, DataError> =
        context.exceptionWrapper {
            val response = api.confirm(data.toRequest())
            val result = response.body()
            val errorResult = response.errorBody()
            when{
                result != null && result is TokenResponse -> {
                    tokenManager.saveToken(result.token)
                    Result.Success(VerificationStatusType.SUCCESS)
                }
                errorResult != null -> {
                    val errorBody = response.errorBody()?.string()

                    val status = errorBody?.let {
                        try {
                            gson.fromJson(it, VerificationStatusResponse::class.java)
                        } catch (e: Exception) {
                            null
                        }
                    }
                    status?.let {
                        Result.Success(it.status)
                    } ?: Result.Error(DataError.Network.SERIALIZATION)
                }
                else -> {
                    val error = when (response.code()) {
                        HttpStatusCode.FORBIDDEN -> DataError.Network.FORBIDDEN
                        HttpStatusCode.NOT_FOUND -> DataError.Network.NOT_FOUND
                        else -> DataError.Network.SERVER_ERROR
                    }
                    Result.Error(error)
                }
            }

        }

}