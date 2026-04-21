package ru.topbun.assistant.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ru.topbun.assistant.AssistantIntent
import ru.topbun.assistant.AssistantState
import ru.topbun.core.ui.components.AppPullRefresh
import ru.topbun.core.ui.components.Height
import ru.topbun.core.ui.components.PaginationList
import ru.topbun.core.ui.theme.Colors

@Composable
internal fun AssistantChatDrawer(
    state: AssistantState,
    onIntent: (AssistantIntent) -> Unit,
    onCloseDrawer: () -> Unit
) {
    Column(
        modifier = Modifier
            .width(320.dp)
            .fillMaxHeight()
            .background(Colors.WHITE)
            .padding(top = 24.dp)
    ) {
        DrawerHeader(
            onClickNewChat = {
                onIntent(AssistantIntent.StartNewChat)
                onCloseDrawer()
            }
        )
        Height(12.dp)
        AppPullRefresh(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            onRefresh = { onIntent(AssistantIntent.RefreshChats) }
        ) {
            PaginationList(
                modifier = Modifier.fillMaxSize(),
                items = state.chatList.chats,
                status = state.chatList.status,
                isEndList = state.chatList.isEndList,
                state = state.chatListState,
                verticalArrangement = Arrangement.spacedBy(12.dp),
                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 12.dp),
                onLoadMore = { onIntent(AssistantIntent.LoadChats) },
                shimmerContent = {
                    items(8) {
                        ChatPreviewShimmer()
                    }
                },
                content = { chats ->
                    items(
                        items = chats,
                        key = { it.id }
                    ) { chat ->
                        ChatPreviewItem(
                            chat = chat,
                            selected = state.selectedChat?.id == chat.id,
                            onClick = {
                                onIntent(AssistantIntent.SelectChat(chat))
                                onCloseDrawer()
                            }
                        )
                    }
                }
            )
        }
    }
}
