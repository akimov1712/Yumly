package ru.topbun.domain.repository.notification

import ru.topbun.common.DataError
import ru.topbun.common.Result
import ru.topbun.domain.entity.notification.NotificationEntity

interface NotificationRepository {

    suspend fun getNotifications(limit: Int, offset: Int): Result<List<NotificationEntity>, DataError>

}