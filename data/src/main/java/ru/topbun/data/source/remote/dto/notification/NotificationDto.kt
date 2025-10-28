package ru.topbun.data.source.remote.dto.notification

import ru.topbun.data.source.remote.dto.account.ProfileDto
import ru.topbun.data.source.remote.dto.recipe.RecipeDto
import ru.topbun.domain.entity.notification.NotificationEntity
import ru.topbun.domain.entity.notification.NotificationType
import java.util.Date

internal data class NotificationDto(
    val id: Int,
    val type: NotificationType,
    val initiator: ProfileDto,
    val recipe: RecipeDto?,
    val createdAt: Date
) {
    fun toEntity() = NotificationEntity(
        id = id,
        type = type,
        initiator = initiator.toEntity(),
        recipe = recipe?.toEntity(),
        createdAt = createdAt
    )

}

internal fun List<NotificationDto>.toEntity() = this.map { it.toEntity() }