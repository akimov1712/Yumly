package ru.topbun.feature.splash

sealed interface SplashEvent {
    data object NavigateToAuth: SplashEvent
    data object NavigateToMain: SplashEvent
}