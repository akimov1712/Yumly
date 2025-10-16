package ru.topbun.data.source.remote.dto.favorite

data class GetFavoriteRequest(
    val limit: Int,
    val offset: Int
)
