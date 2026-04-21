package ru.topbun.assistant.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ru.topbun.assistant.AssistantIntent
import ru.topbun.assistant.AssistantState
import ru.topbun.core.ui.components.AppPullRefresh
import ru.topbun.core.ui.components.Height
import ru.topbun.core.ui.components.PaginationList

@Composable
internal fun AssistantHistorySheet(
    state: AssistantState,
    onIntent: (AssistantIntent) -> Unit,
    onClose: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(0.86f)
            .padding(top = 8.dp)
    ) {
        HistoryHeader(
            onClickNewChat = {
                onIntent(AssistantIntent.StartNewChat)
                onClose()
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
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
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
                                onClose()
                            }
                        )
                    }
                }
            )
        }
    }
}
