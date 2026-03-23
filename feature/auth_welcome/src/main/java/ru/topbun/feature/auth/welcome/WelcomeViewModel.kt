package ru.topbun.feature.auth.welcome

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.topbun.core.ui.utils.handleIntent
import ru.topbun.domain.useCases.config.SetStatusFirstStartUseCase

class WelcomeViewModel(
    private val setStatusFirstStartUseCase: SetStatusFirstStartUseCase
) : ViewModel() {

    private val _intent = Channel<WelcomeIntent>(Channel.UNLIMITED)

    private val _state = MutableStateFlow(WelcomeState())
    val state = _state.asStateFlow()

    private val _events = Channel<WelcomeEvent>()
    val events = _events.receiveAsFlow()

    private fun onContinue(){}
    private fun onSkip(){}

    private fun handleIntent() = _intent.handleIntent(viewModelScope) {
        when(it){
            WelcomeIntent.Continue -> onContinue()
            WelcomeIntent.Skip -> onSkip()
        }
    }

    fun sendIntent(intent: WelcomeIntent) = viewModelScope.launch {
        _intent.send(intent)
    }

    init {
        handleIntent()
    }

}
