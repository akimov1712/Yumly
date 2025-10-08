package ru.topbun.domain.entity.notification

import ru.topbun.domain.entity.account.UserEntity
import ru.topbun.domain.entity.recipe.RecipeEntity
import java.util.Date

data class NotificationEntity(
    val id: Int,
    val type: NotificationType,
    val initiator: UserEntity,
    val recipe: RecipeEntity?,
    val createdAt: Date
)
