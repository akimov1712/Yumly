package ru.topbun.core.ui.utils

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.launch

fun <T> Channel<T>.handleIntent(scope: CoroutineScope, callbackIntent: suspend (T) -> Unit) = scope.launch {
    this@handleIntent.consumeAsFlow().collect{ callbackIntent(it) }
}