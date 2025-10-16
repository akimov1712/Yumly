package ru.topbun.data.source.remote.api.follow

import retrofit2.Response
import retrofit2.http.POST
import retrofit2.http.Path

interface FollowApi {

    @POST("/v1/follow/{id}")
    fun switchFollowStatus(@Path("id") userId: Int): Response<Boolean>

}