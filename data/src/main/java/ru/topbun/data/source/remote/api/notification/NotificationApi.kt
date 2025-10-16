package ru.topbun.data.source.remote.api.notification

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST
import ru.topbun.data.source.remote.dto.notification.GetNotificationRequest
import ru.topbun.data.source.remote.dto.notification.GetNotificationResponse

internal interface NotificationApi {

    @POST("/v1/notification")
    suspend fun getNotifications(@Body body: GetNotificationRequest): Response<GetNotificationResponse>

}