package ru.topbun.data.source.remote.dto.account

import ru.topbun.domain.entity.account.ProfileEntity

internal data class ProfileDto(
    val userId: Int,
    val username: String,
    val email: String,
    val photoUrl: String?,
    val countRecipes: Int,
    val countFollowing: Int,
    val countFollowers: Int,
    val countLikes: Int,
    val isFollow: Boolean
){

    fun toEntity() = ProfileEntity(
        userId = userId,
        username = username,
        email = email,
        photoUrl = photoUrl,
        countRecipes = countRecipes,
        countFollowing = countFollowing,
        countFollowers = countFollowers,
        countLikes = countLikes,
        isFollow = isFollow,
    )

}

internal fun List<ProfileDto>.toEntity() = this.map { it.toEntity() }
