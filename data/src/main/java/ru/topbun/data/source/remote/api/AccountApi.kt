package ru.topbun.data.source.remote.api

import okhttp3.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import ru.topbun.data.source.remote.dto.account.ResetPasswordRequest
import ru.topbun.data.source.remote.dto.account.UserDto

interface AccountApi {

    @POST("/v1/account/reset-password")
    suspend fun resetPassword(@Body body: ResetPasswordRequest): Response

    @GET("/v1/account/info")
    suspend fun getAccountInfo(): retrofit2.Response<UserDto>

}