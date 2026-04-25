package ru.topbun.navigation

import cafe.adriel.voyager.core.registry.ScreenProvider

sealed interface ProfileScreenProvider : ScreenProvider {

    data class User(val userId: Int) : ProfileScreenProvider
    object Settings : ProfileScreenProvider

}
