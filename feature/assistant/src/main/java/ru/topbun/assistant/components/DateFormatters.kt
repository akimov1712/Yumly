package ru.topbun.assistant.components

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

internal fun LocalDateTime.toChatDate(): String =
    format(CHAT_DATE_FORMATTER)

internal fun LocalDateTime.toMessageDate(): String =
    format(MESSAGE_DATE_FORMATTER)

private val CHAT_DATE_FORMATTER = DateTimeFormatter.ofPattern("dd MMM, HH:mm", Locale.forLanguageTag("ru"))
private val MESSAGE_DATE_FORMATTER = DateTimeFormatter.ofPattern("HH:mm", Locale.forLanguageTag("ru"))
