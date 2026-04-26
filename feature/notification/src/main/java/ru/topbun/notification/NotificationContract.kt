package ru.topbun.notification

import androidx.compose.foundation.lazy.LazyListState
import ru.topbun.domain.ScreenUiState
import ru.topbun.domain.entity.notification.NotificationEntity
import java.time.LocalDate

internal data class NotificationState(
    val notificationUiState: NotificationUiState? = null,
    val list: NotificationListUiState = NotificationListUiState(),
    val listState: LazyListState = LazyListState(),
) {

    val groups: List<NotificationGroup>
        get() = list.items
            .groupBy { it.createdAt.toLocalDate() }
            .toSortedMap(compareByDescending { it })
            .map { (date, items) ->
                NotificationGroup(
                    date = date,
                    items = items.sortedByDescending { it.createdAt }
                )
            }

    enum class NotificationUiState {
        SUCCESS, NEED_AUTH
    }

    data class NotificationListUiState(
        val items: List<NotificationEntity> = emptyList(),
        val status: ScreenUiState = ScreenUiState.Idle,
        val isEndList: Boolean = false,
    )

    data class NotificationGroup(
        val date: LocalDate,
        val items: List<NotificationEntity>
    )

}

internal sealed interface NotificationIntent {

    data object CheckSession : NotificationIntent
    data object LoadNotifications : NotificationIntent
    data object RefreshNotifications : NotificationIntent
    data class ClickInitiator(val userId: Int) : NotificationIntent
    data class ClickRecipe(val recipeId: Int, val authorUserId: Int) : NotificationIntent

}

internal sealed interface NotificationEvent {

    data class NavigateToProfile(val userId: Int) : NotificationEvent
    data class NavigateToRecipe(val recipeId: Int, val authorUserId: Int) : NotificationEvent

}
