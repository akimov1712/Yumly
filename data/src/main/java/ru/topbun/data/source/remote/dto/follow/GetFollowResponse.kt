package ru.topbun.data.source.remote.dto.follow

import ru.topbun.data.source.remote.dto.account.ProfileDto

internal data class GetFollowResponse(
    val count: Int,
    val follows: List<ProfileDto>
)
