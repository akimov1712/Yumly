package ru.topbun.feature.auth.welcome

sealed interface WelcomeEvent {
    data object NavigateToLogin : WelcomeEvent
}
