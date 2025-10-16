package ru.topbun.data.source.remote.dto.notification

internal data class GetNotificationRequest(
    val limit: Int,
    val offset: Int,
)
