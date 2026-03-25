package ru.topbun.core.android

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

fun <T> Channel<T>.handleIntent(scope: CoroutineScope, callbackIntent: suspend (T) -> Unit) = scope.launch {
    this@handleIntent.consumeAsFlow().collect{ callbackIntent(it) }
}

abstract class MVI<I,S,E>(state: S): ViewModel() {

    protected val _intent = Channel<I>(Channel.UNLIMITED)

    protected val _state = MutableStateFlow(state)
    val state = _state.asStateFlow()

    protected val _events = Channel<E>()
    val events = _events.receiveAsFlow()

    fun sendIntent(intent: I) = viewModelScope.launch {
        _intent.send(intent)
    }

    protected abstract suspend fun handleIntent(intent: I)

    protected fun observeIntent() = _intent.handleIntent(viewModelScope) {
        handleIntent(it)
    }

    init {
        observeIntent()
    }

}

