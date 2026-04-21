package ru.topbun.assistant.components

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

internal fun Date.toChatDate(): String =
    SimpleDateFormat("dd MMM, HH:mm", Locale.forLanguageTag("ru")).format(this)

internal fun Date.toMessageDate(): String =
    SimpleDateFormat("HH:mm", Locale.forLanguageTag("ru")).format(this)
