package ru.topbun.core.android

import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import org.koin.dsl.module

val snackbarModule = module {
    single { SnackbarManager() }
}

class SnackbarManager {

    private val _messages = Channel<String>()
    val messages = _messages.receiveAsFlow()

    fun showMessage(message: String){
        _messages.trySend(message)
    }

}