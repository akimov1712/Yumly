package ru.topbun.data.source.remote.api.verification

import okhttp3.Response
import retrofit2.http.Body
import retrofit2.http.POST
import ru.topbun.data.source.remote.dto.verification.ConfirmVerificationRequest
import ru.topbun.data.source.remote.dto.verification.RequestVerificationRequest

internal interface VerificationApi {

    @POST("/v1/verification/request")
    suspend fun request(@Body body: RequestVerificationRequest): retrofit2.Response<Unit>

    @POST("/v1/verification/confirm")
    suspend fun confirm(@Body body: ConfirmVerificationRequest): Response

}