package ru.topbun.data.source.remote.dto.favorite

internal data class GetFavoriteRequest(
    val limit: Int,
    val offset: Int
)
