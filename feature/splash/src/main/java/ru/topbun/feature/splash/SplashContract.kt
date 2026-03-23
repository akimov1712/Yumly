package ru.topbun.feature.splash

import ru.topbun.navigation.auth.AuthStartScreen

sealed interface SplashEvent {
    data class NavigateToAuth(val startDestination: AuthStartScreen): SplashEvent
    data object NavigateToMain: SplashEvent
}
