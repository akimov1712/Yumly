package ru.topbun.auth_welcome

import ru.topbun.core.android.MVI
import ru.topbun.domain.useCases.config.SetStatusFirstStartUseCase

class WelcomeViewModel(
    private val setStatusFirstStartUseCase: SetStatusFirstStartUseCase
) : MVI<WelcomeIntent, Unit, WelcomeEvent>(Unit) {

    override suspend fun handleIntent(intent: WelcomeIntent) {
        when (intent) {
            WelcomeIntent.Continue -> onContinue()
            WelcomeIntent.Skip -> onSkip()
        }
    }

    private suspend fun onContinue() {
        _events.send(WelcomeEvent.NavigateToLogin)
    }

    private suspend fun onSkip() {
        setStatusFirstStartUseCase(false)
        _events.send(WelcomeEvent.NavigateToDashboard)
    }


}
