package ru.topbun.domain.entity.notification

import ru.topbun.domain.entity.account.ProfileEntity
import ru.topbun.domain.entity.account.UserEntity
import ru.topbun.domain.entity.recipe.RecipeEntity
import java.time.LocalDateTime
import java.util.Date

data class NotificationEntity(
    val id: Int,
    val type: NotificationType,
    val initiator: ProfileEntity,
    val recipe: RecipeEntity?,
    val createdAt: LocalDateTime
)
