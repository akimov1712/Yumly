package ru.topbun.notification.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ru.topbun.core.ui.components.AppButton
import ru.topbun.core.ui.components.AppPullRefresh
import ru.topbun.core.ui.theme.Colors
import ru.topbun.core.ui.utils.getBottomBarPadding
import ru.topbun.domain.ScreenUiState
import ru.topbun.notification.NotificationState

@Composable
internal fun NotificationList(
    state: LazyListState,
    status: ScreenUiState,
    isEndList: Boolean,
    groups: List<NotificationState.NotificationGroup>,
    onLoadMore: () -> Unit,
    onRefresh: () -> Unit,
    onClickInitiator: (userId: Int) -> Unit,
    onClickRecipe: (recipeId: Int, authorUserId: Int) -> Unit,
    headerContent: @Composable () -> Unit,
) {
    PreloadTrigger(
        state = state,
        status = status,
        isEndList = isEndList,
        onLoadMore = onLoadMore
    )

    AppPullRefresh(
        modifier = Modifier.fillMaxSize(),
        onRefresh = onRefresh
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            state = state,
            contentPadding = PaddingValues(
                start = 12.dp,
                end = 12.dp,
                bottom = getBottomBarPadding()
            ),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            item("notification_header") { headerContent() }

            when {
                groups.isEmpty() && status.isLoading -> {
                    items(4) { index ->
                        NotificationShimmer(showRecipePreview = index % 2 == 0)
                    }
                }
                groups.isEmpty() && status.isSuccess -> item("notification_empty") {
                    EmptyContent()
                }
                groups.isEmpty() && status.isError -> item("notification_error") {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 24.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        AppButton(
                            text = "Загрузить снова",
                            onClick = onLoadMore
                        )
                    }
                }
                else -> {
                    groups.forEach { group ->
                        item(key = "group_title_${group.date}") {
                            GroupTitle(title = formatGroupTitle(group.date))
                        }
                        items(
                            items = group.items,
                            key = { item -> "notif_${item.id}" }
                        ) { notification ->
                            NotificationItem(
                                notification = notification,
                                onClickInitiator = {
                                    onClickInitiator(notification.initiator.userId)
                                },
                                onClickRecipe = {
                                    val recipeId = notification.recipe?.id ?: return@NotificationItem
                                    onClickRecipe(recipeId, notification.initiator.userId)
                                }
                            )
                        }
                    }
                }
            }

            when {
                status.isLoading && groups.isNotEmpty() && !isEndList -> item("loading_footer") {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(24.dp),
                            strokeWidth = 2.5.dp,
                            trackColor = Colors.PRIMARY
                        )
                    }
                }
                status.isError && groups.isNotEmpty() -> item("error_footer") {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        AppButton(
                            text = "Загрузить снова",
                            onClick = onLoadMore
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun PreloadTrigger(
    state: LazyListState,
    status: ScreenUiState,
    isEndList: Boolean,
    onLoadMore: () -> Unit,
) {
    val shouldLoadMore = remember(state) {
        derivedStateOf {
            val layoutInfo = state.layoutInfo
            val lastVisibleItemIndex =
                layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: return@derivedStateOf false
            val totalItemsCount = layoutInfo.totalItemsCount
            lastVisibleItemIndex >= totalItemsCount - 3
        }
    }

    LaunchedEffect(shouldLoadMore.value) {
        if (
            shouldLoadMore.value &&
            !status.isLoading &&
            !status.isError &&
            !isEndList
        ) {
            onLoadMore()
        }
    }
}
