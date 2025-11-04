package ru.topbun.navigation

import cafe.adriel.voyager.core.registry.ScreenProvider

sealed interface RootScreenProvider : ScreenProvider {

    object Splash: RootScreenProvider
    object Auth: RootScreenProvider
    object Main: RootScreenProvider

}