package ru.topbun.navigation

import cafe.adriel.voyager.core.registry.ScreenProvider

sealed interface ProfileScreenProvider : ScreenProvider {

    data class User(val userId: Int) : ProfileScreenProvider
    object Settings : ProfileScreenProvider
    data class Follows(val userId: Int, val initialTab: FollowsTab) : ProfileScreenProvider

    enum class FollowsTab { Followers, Following }

}
