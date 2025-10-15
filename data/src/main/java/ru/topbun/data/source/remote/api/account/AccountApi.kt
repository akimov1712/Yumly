package ru.topbun.data.source.remote.api.account

import okhttp3.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import ru.topbun.data.source.remote.dto.account.ProfileDto
import ru.topbun.data.source.remote.dto.account.ResetPasswordRequest
import ru.topbun.data.source.remote.dto.account.UpdateAccountInfoRequest
import ru.topbun.data.source.remote.dto.account.UserDto

interface AccountApi {

    @POST("/v1/account/reset-password")
    suspend fun resetPassword(@Body body: ResetPasswordRequest): Response

    @GET("/v1/account/info")
    suspend fun getAccountInfo(): retrofit2.Response<UserDto>

    @PUT("/v1/account/info")
    suspend fun updateAccountInfo(@Body body: UpdateAccountInfoRequest): retrofit2.Response<UserDto>

    @GET("/v1/profile/info/{id}")
    suspend fun getProfile(@Path("id") userId: Int): retrofit2.Response<ProfileDto>

}