package ru.topbun.assistant.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.valentinilk.shimmer.ShimmerBounds
import com.valentinilk.shimmer.rememberShimmer
import com.valentinilk.shimmer.shimmer
import ru.topbun.assistant.AssistantIntent
import ru.topbun.assistant.AssistantState
import ru.topbun.core.ui.R
import ru.topbun.core.ui.components.AppButton
import ru.topbun.core.ui.components.AppPullRefresh
import ru.topbun.core.ui.components.AppTextButton
import ru.topbun.core.ui.components.AppTextField
import ru.topbun.core.ui.components.Height
import ru.topbun.core.ui.components.PaginationList
import ru.topbun.core.ui.components.Width
import ru.topbun.core.ui.theme.Colors
import ru.topbun.core.ui.theme.Typography
import ru.topbun.core.ui.utils.LocalBottomBarPadding
import ru.topbun.core.ui.utils.rippleClickable
import ru.topbun.domain.ScreenUiState
import ru.topbun.domain.entity.gpt.GptChatEntity
import ru.topbun.domain.entity.gpt.GptMessageEntity
import ru.topbun.domain.entity.gpt.GptMessageRoleType
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
internal fun AssistantTopBar(
    onClickChats: () -> Unit
) {
    Row(
        modifier = Modifier
            .padding(horizontal = 12.dp)
            .padding(top = 20.dp)
            .fillMaxWidth()
            .clip(RoundedCornerShape(44.dp))
            .background(Colors.WHITE)
            .padding(horizontal = 10.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        AppTextButton(
            text = "Чаты",
            modifier = Modifier.defaultMinSize(minHeight = 48.dp),
            textColor = Colors.BLUE_TEXT,
            containerColor = Colors.BLUE_TEXT,
            onClick = onClickChats
        )
        Spacer(Modifier.weight(1f))
        Text(
            text = "Assistant",
            color = Colors.MAIN_TEXT,
            style = Typography.H2,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
        Width(20.dp)
        Icon(
            modifier = Modifier.size(28.dp),
            painter = painterResource(R.drawable.ic_tabs_assistant),
            contentDescription = null,
            tint = Colors.PRIMARY
        )
    }
}

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

@Composable
private fun DrawerHeader(
    onClickNewChat: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp)
    ) {
        Text(
            text = "Ваши чаты",
            color = Colors.MAIN_TEXT,
            style = Typography.H1
        )
        Height(4.dp)
        Text(
            text = "Выберите диалог или начните новый запрос",
            color = Colors.SECONDARY_TEXT,
            style = Typography.P2
        )
        Height(16.dp)
        AppButton(
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp),
            text = "Новый чат",
            onClick = onClickNewChat
        )
    }
}

@Composable
private fun ChatPreviewItem(
    chat: GptChatEntity,
    selected: Boolean,
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(28.dp))
            .background(if (selected) Colors.PRIMARY.copy(alpha = 0.12f) else Colors.FORM)
            .rippleClickable(onClick = onClick)
            .padding(16.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                modifier = Modifier.weight(1f),
                text = "Чат #${chat.id}",
                color = Colors.BLUE_TEXT,
                style = Typography.H3,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = "${chat.messages.size}/${chat.maxLimitMessages}",
                color = Colors.SECONDARY_TEXT,
                style = Typography.S
            )
        }
        Height(8.dp)
        Text(
            text = chat.messages.firstOrNull()?.text?.ifBlank { "Без текста" } ?: "Новый диалог",
            color = Colors.MAIN_TEXT,
            style = Typography.P2.copy(lineHeight = 20.sp),
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
internal fun AssistantPlaceholder(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 32.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(156.dp)
                .clip(CircleShape)
                .background(Colors.WHITE),
            contentAlignment = Alignment.Center
        ) {
            Box(
                modifier = Modifier
                    .size(104.dp)
                    .clip(CircleShape)
                    .background(Colors.PRIMARY.copy(alpha = 0.14f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    modifier = Modifier.size(60.dp),
                    painter = painterResource(R.drawable.ic_tabs_assistant),
                    contentDescription = null,
                    tint = Colors.PRIMARY
                )
            }
        }
        Height(28.dp)
        Text(
            text = "Yumly Assistant",
            color = Colors.MAIN_TEXT,
            style = Typography.H1,
            textAlign = TextAlign.Center
        )
        Height(10.dp)
        Text(
            text = "Поможет придумать блюдо, подобрать ингредиенты, уточнить шаги рецепта и быстро ответить на вопросы по готовке.",
            color = Colors.BLUE_TEXT,
            style = Typography.P2,
            textAlign = TextAlign.Center
        )
        Height(18.dp)
        Text(
            text = "Напишите первый вопрос, и ассистент создаст новый чат.",
            color = Colors.SECONDARY_TEXT,
            style = Typography.S,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
internal fun MessageList(
    messages: List<GptMessageEntity>,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 16.dp)
    ) {
        if (messages.isEmpty()) {
            items(4) {
                MessageShimmer()
            }
        } else {
            items(
                items = messages,
                key = { it.id }
            ) { message ->
                MessageBubble(message)
            }
        }
    }
}

@Composable
private fun MessageBubble(message: GptMessageEntity) {
    val isUser = message.role == GptMessageRoleType.USER
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = if (isUser) Arrangement.End else Arrangement.Start
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth(0.82f)
                .clip(
                    RoundedCornerShape(
                        topStart = 24.dp,
                        topEnd = 24.dp,
                        bottomStart = if (isUser) 24.dp else 8.dp,
                        bottomEnd = if (isUser) 8.dp else 24.dp
                    )
                )
                .background(if (isUser) Colors.PRIMARY else Colors.WHITE)
                .padding(16.dp)
        ) {
            Text(
                text = message.text,
                color = if (isUser) Colors.WHITE else Colors.MAIN_TEXT,
                style = Typography.P2
            )
            Height(8.dp)
            Text(
                modifier = Modifier.align(if (isUser) Alignment.End else Alignment.Start),
                text = message.createdAt.toMessageDate(),
                color = if (isUser) Colors.WHITE.copy(alpha = 0.72f) else Colors.SECONDARY_TEXT,
                style = Typography.S
            )
        }
    }
}

@Composable
internal fun AssistantInputBar(
    text: String,
    enabled: Boolean,
    isLoading: Boolean,
    onValueChange: (String) -> Unit,
    onSend: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp)
            .padding(bottom = LocalBottomBarPadding.current)
            .clip(RoundedCornerShape(32.dp))
            .background(Colors.WHITE)
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        AppTextField(
            modifier = Modifier
                .weight(1f)
                .defaultMinSize(minHeight = 56.dp),
            text = text,
            placeholder = "Сообщение ассистенту",
            singleLine = false,
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Default),
            onValueChange = onValueChange
        )
        AppButton(
            modifier = Modifier
                .height(56.dp)
                .width(118.dp),
            text = "Отправить",
            enabled = enabled,
            isLoading = isLoading,
            onClick = onSend
        )
    }
}

@Composable
internal fun MessageLimitBlock() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp)
            .padding(bottom = LocalBottomBarPadding.current)
            .clip(RoundedCornerShape(28.dp))
            .background(Colors.WHITE)
            .padding(18.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "Достигнут лимит сообщений. Начните новый чат.",
            color = Colors.BLUE_TEXT,
            style = Typography.H3,
            textAlign = TextAlign.Center
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

@Composable
private fun MessageShimmer() {
    val shimmer = rememberShimmer(shimmerBounds = ShimmerBounds.Window)
    Box(
        modifier = Modifier
            .fillMaxWidth(0.82f)
            .height(72.dp)
            .clip(RoundedCornerShape(24.dp))
            .shimmer(shimmer)
            .background(Colors.WHITE)
    )
}

private fun Date.toChatDate(): String =
    SimpleDateFormat("dd MMM, HH:mm", Locale.forLanguageTag("ru")).format(this)

private fun Date.toMessageDate(): String =
    SimpleDateFormat("HH:mm", Locale.forLanguageTag("ru")).format(this)
