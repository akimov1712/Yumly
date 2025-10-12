package ru.topbun.data.source.remote.api

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST
import ru.topbun.data.source.remote.dto.signUp.SignUpRequest
import ru.topbun.data.source.remote.dto.account.UserDto

interface SignUpApi {

    @POST("/v1/signUp")
    suspend fun signUp(@Body body: SignUpRequest): Response<UserDto>

}