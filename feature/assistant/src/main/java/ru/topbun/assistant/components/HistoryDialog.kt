package ru.topbun.assistant.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.valentinilk.shimmer.ShimmerBounds
import com.valentinilk.shimmer.rememberShimmer
import com.valentinilk.shimmer.shimmer
import ru.topbun.assistant.AssistantIntent
import ru.topbun.assistant.AssistantState
import ru.topbun.core.ui.components.AppButton
import ru.topbun.core.ui.components.AppPullRefresh
import ru.topbun.core.ui.components.BottomDialogWrapper
import ru.topbun.core.ui.components.Height
import ru.topbun.core.ui.components.PaginationList
import ru.topbun.core.ui.components.Width
import ru.topbun.core.ui.theme.Colors
import ru.topbun.core.ui.theme.Typography
import ru.topbun.core.ui.utils.rippleClickable
import ru.topbun.domain.entity.gpt.GptChatEntity

@Composable
internal fun HistoryDialog(
    state: AssistantState,
    onDismissRequest: () -> Unit,
    onIntent: (AssistantIntent) -> Unit,
) = BottomDialogWrapper(
    onDismissRequest = onDismissRequest,
    containerColor = Colors.FORM
){
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Colors.BACKGROUND)
            .padding(bottom = 8.dp)
    ) {
        HistoryHeader(
            onClickNewChat = {
                onIntent(AssistantIntent.StartNewChat)
                onDismissRequest()
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
                            selected = state.activeChatId == chat.id,
                            onClick = {
                                onIntent(AssistantIntent.SelectChat(chat))
                                onDismissRequest()
                            }
                        )
                    }
                }
            )
        }
    }
}


@Composable
private fun HistoryHeader(
    onClickNewChat: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = "История чатов",
                color = Colors.MAIN_TEXT,
                style = Typography.H1
            )
            Height(4.dp)
            Text(
                text = "Новые диалоги всегда сверху",
                color = Colors.SECONDARY_TEXT,
                style = Typography.S
            )
        }
        AppButton(
            modifier = Modifier.height(48.dp),
            text = "Новый",
            onClick = onClickNewChat
        )
    }
}


@Composable
private  fun ChatPreviewItem(
    chat: GptChatEntity,
    selected: Boolean,
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(24.dp))
            .background(if (selected) Colors.PRIMARY.copy(alpha = 0.14f) else Colors.WHITE)
            .rippleClickable(color = Colors.BLACK, onClick = onClick)
            .padding(16.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(10.dp)
                    .clip(CircleShape)
                    .background(if (selected) Colors.PRIMARY else Colors.SECONDARY_TEXT.copy(alpha = 0.45f))
            )
            Width(10.dp)
            Text(
                modifier = Modifier.weight(1f),
                text = "Чат #${chat.id}",
                color = Colors.BLUE_TEXT,
                style = ru.topbun.core.ui.theme.Typography.H3,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
        Height(8.dp)
        Text(
            text = chat.messages.firstOrNull()?.text?.toPlainMessagePreview()?.ifBlank { "Без текста" } ?: "Новый диалог",
            color = Colors.MAIN_TEXT,
            style = ru.topbun.core.ui.theme.Typography.P2.copy(lineHeight = 20.sp),
            maxLines = 2,
            overflow = TextOverflow.Ellipsis
        )
        Height(10.dp)
        Text(
            text = chat.createdAt.toChatDate(),
            color = Colors.SECONDARY_TEXT,
            style = Typography.S
        )
    }
}



@Composable
private fun ChatPreviewShimmer() {
    val shimmer = rememberShimmer(shimmerBounds = ShimmerBounds.Window)
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(28.dp))
            .background(Colors.FORM)
            .padding(16.dp)
    ) {
        Box(
            Modifier
                .fillMaxWidth(0.52f)
                .height(22.dp)
                .clip(RoundedCornerShape(10.dp))
                .shimmer(shimmer)
                .background(Colors.SHIMMER)
        )
        Height(12.dp)
        Box(
            Modifier
                .fillMaxWidth()
                .height(18.dp)
                .clip(RoundedCornerShape(9.dp))
                .shimmer(shimmer)
                .background(Colors.SHIMMER)
        )
        Height(7.dp)
        Box(
            Modifier
                .fillMaxWidth(0.74f)
                .height(18.dp)
                .clip(RoundedCornerShape(9.dp))
                .shimmer(shimmer)
                .background(Colors.SHIMMER)
        )
    }
}
