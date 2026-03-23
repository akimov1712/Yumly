package ru.topbun.feature.auth.welcome

sealed interface WelcomeIntent{
    data object Continue: WelcomeIntent
    data object Skip: WelcomeIntent
}