package ru.topbun.domain.useCases.notification

import ru.topbun.domain.repository.notification.NotificationRepository

class GetNotificationsUseCase(
    private val repository: NotificationRepository
) {

    suspend operator fun invoke(limit: Int = 20, offset: Int = 0) = repository.getNotifications(limit, offset)

}