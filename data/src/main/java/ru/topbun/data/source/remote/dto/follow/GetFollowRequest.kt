package ru.topbun.data.source.remote.dto.follow

data class GetFollowRequest(
    val followId: Int,
    val limit: Int,
    val offset: Int
)
