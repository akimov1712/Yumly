package ru.topbun.data.source.remote.api.follow

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Path
import ru.topbun.data.source.remote.dto.follow.GetFollowRequest
import ru.topbun.data.source.remote.dto.follow.GetFollowResponse

internal interface FollowApi {

    @POST("/v1/follow/{id}")
    suspend fun switchFollowStatus(@Path("id") userId: Int): Response<Boolean>

    @POST("/v1/follow/followers")
    suspend fun getFollowers(@Body body: GetFollowRequest): Response<GetFollowResponse>


    @POST("/v1/follow/following")
    suspend fun getFollowing(@Body body: GetFollowRequest): Response<GetFollowResponse>

}