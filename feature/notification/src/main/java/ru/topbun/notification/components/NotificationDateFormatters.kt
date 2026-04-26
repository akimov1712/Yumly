package ru.topbun.notification.components

import java.time.LocalDate
import java.time.LocalDateTime

private val MONTHS_GENITIVE = listOf(
    "января", "февраля", "марта", "апреля", "мая", "июня",
    "июля", "августа", "сентября", "октября", "ноября", "декабря"
)

internal fun formatGroupTitle(date: LocalDate, today: LocalDate = LocalDate.now()): String {
    val daysBetween = java.time.temporal.ChronoUnit.DAYS.between(date, today)
    val month = MONTHS_GENITIVE[date.monthValue - 1]
    return when {
        daysBetween == 0L -> "Сегодня"
        daysBetween == 1L -> "Вчера"
        date.year != today.year -> "${date.dayOfMonth} $month ${date.year}"
        else -> "${date.dayOfMonth} $month"
    }
}

internal fun formatNotificationTime(dateTime: LocalDateTime): String {
    val hour = dateTime.hour.toString().padStart(2, '0')
    val minute = dateTime.minute.toString().padStart(2, '0')
    return "$hour:$minute"
}
