package ru.topbun.data.repository.notification

import android.content.Context
import ru.topbun.common.HttpStatusCode
import ru.topbun.common.Result
import ru.topbun.common.error.DataError
import ru.topbun.data.exceptionWrapper
import ru.topbun.data.source.remote.api.notification.NotificationApi
import ru.topbun.data.source.remote.dto.notification.GetNotificationRequest
import ru.topbun.data.source.remote.dto.notification.GetNotificationResponse
import ru.topbun.data.source.remote.dto.notification.toEntity
import ru.topbun.domain.entity.notification.NotificationEntity
import ru.topbun.domain.repository.notification.NotificationRepository

internal class NotificationRepositoryImpl(
    private val context: Context,
    private val api: NotificationApi
): NotificationRepository {

    override suspend fun getNotifications(limit: Int, offset: Int): Result<List<NotificationEntity>, DataError> =
        context.exceptionWrapper {
            val request = GetNotificationRequest(limit, offset)
            val response = api.getNotifications(request)
            val notifications = response.body()
            if (response.isSuccessful && notifications != null){
                Result.Success(notifications.toEntity())
            } else {
                val error = when(response.code()){
                    HttpStatusCode.UNAUTHORIZED -> DataError.Network.UNAUTHORIZED
                    else -> DataError.Network.SERVER_ERROR
                }
                Result.Error(error)
            }
        }

}