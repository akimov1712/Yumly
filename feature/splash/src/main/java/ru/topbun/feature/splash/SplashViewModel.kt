package ru.topbun.feature.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import ru.topbun.domain.useCases.config.GetStatusFirstStartUseCase
import ru.topbun.domain.useCases.session.HasSessionUseCase
import ru.topbun.navigation.auth.AuthStartScreen

class SplashViewModel(
    private val getStatusFirstStartUseCase: GetStatusFirstStartUseCase,
): ViewModel() {

    private val _events = Channel<SplashEvent>()
    val events get() = _events.receiveAsFlow()

    init {
        handleFirstStart()
    }

    private fun handleFirstStart() = viewModelScope.launch {
        val firstStatus = getStatusFirstStartUseCase()
        delay(1500)
        val event = when{
            firstStatus -> SplashEvent.NavigateToAuth(AuthStartScreen.WELCOME)
            else -> SplashEvent.NavigateToMain
        }
        _events.send(event)
    }

}
