package ru.topbun.domain.entity.account

data class ProfileEntity(
    val userId: Int,
    val username: String,
    val email: String,
    val photoUrl: String?,
    val countRecipes: Int,
    val countFollowing: Int,
    val countFollowers: Int,
    val countLikes: Int,
    val isFollow: Boolean
)
