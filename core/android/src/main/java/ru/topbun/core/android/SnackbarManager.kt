package ru.topbun.core.android

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import org.koin.dsl.module

val snackbarModule = module {
    single { SnackbarManager() }
}

class SnackbarManager {

    private val _messages = Channel<String>()
    val messages = _messages.receiveAsFlow()

    fun sendMessage(message: String){
        _messages.trySend(message)
    }

}