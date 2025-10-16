package ru.topbun.data.source.remote.api.login

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST
import ru.topbun.data.source.remote.dto.login.LoginRequest
import ru.topbun.data.source.remote.dto.token.TokenResponse

internal interface LoginApi {


    @POST("/v1/login")
    suspend fun login(@Body body: LoginRequest): Response<TokenResponse>

}