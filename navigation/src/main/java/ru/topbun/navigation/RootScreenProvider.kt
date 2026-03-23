package ru.topbun.navigation

import cafe.adriel.voyager.core.registry.ScreenProvider
import ru.topbun.navigation.auth.AuthStartScreen

sealed interface RootScreenProvider : ScreenProvider {

    object Splash: RootScreenProvider
    data class Auth(val startScreen: AuthStartScreen): RootScreenProvider
    object Dashboard: RootScreenProvider

}
