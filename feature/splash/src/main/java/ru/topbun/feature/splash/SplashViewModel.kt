package ru.topbun.feature.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import ru.topbun.domain.useCases.config.GetStatusFirstStartUseCase
import ru.topbun.domain.useCases.config.SetStatusFirstStartUseCase

class SplashViewModel(
    private val getStatusFirstStartUseCase: GetStatusFirstStartUseCase,
    private val setStatusFirstStartUseCase: SetStatusFirstStartUseCase
): ViewModel() {

    private val _events = Channel<SplashEvent>()
    val events get() = _events.receiveAsFlow()

    init {
        handleFirstStart()
    }

    private fun handleFirstStart() = viewModelScope.launch {
        val status = getStatusFirstStartUseCase()
        if (status){
            setStatusFirstStartUseCase(false)
            _events.send(SplashEvent.NavigateToAuth)
        } else {
            _events.send(SplashEvent.NavigateToMain)
        }
    }

}