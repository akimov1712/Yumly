package ru.topbun.auth_welcome


sealed interface WelcomeIntent{
    data object Continue: WelcomeIntent
    data object Skip: WelcomeIntent
}

sealed interface WelcomeEvent {
    data object NavigateToLogin : WelcomeEvent
    data object NavigateToDashboard : WelcomeEvent
}
