package ru.topbun.data.source.remote.api

import okhttp3.Response
import retrofit2.http.Body
import retrofit2.http.POST
import ru.topbun.data.source.remote.dto.verification.RequestVerificationRequest

interface VerificationApi {

    @POST("/v1/request")
    suspend fun request(@Body body: RequestVerificationRequest): Response

}