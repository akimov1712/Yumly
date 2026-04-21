package ru.topbun.assistant.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.background
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import ru.topbun.core.ui.theme.Colors
import ru.topbun.core.ui.theme.Typography
import ru.topbun.core.ui.utils.rippleClickable
import ru.topbun.domain.entity.gpt.GptChatEntity

@Composable
internal fun RecentChatsSection(
    chats: List<GptChatEntity>,
    onClickChat: (GptChatEntity) -> Unit,
    onClickAll: () -> Unit
) {
    LazyRow(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 12.dp)
    ) {
        item {
            RecentChatsTitle(onClick = onClickAll)
        }
        items(
            items = chats,
            key = { it.id }
        ) { chat ->
            RecentChatItem(
                chat = chat,
                onClick = { onClickChat(chat) }
            )
        }
    }
}

@Composable
private fun RecentChatsTitle(
    onClick: () -> Unit
) {
    RecentChatCard(
        title = "Все чаты",
        subtitle = "Открыть историю",
        selected = true,
        onClick = onClick
    )
}

@Composable
internal fun RecentChatItem(
    chat: GptChatEntity,
    onClick: () -> Unit
) {
    val preview = chat.messages.firstOrNull()?.text?.toPlainMessagePreview()?.ifBlank { "Без текста" } ?: "Новый диалог"
    RecentChatCard(
        title = "Чат #${chat.id}",
        subtitle = preview,
        selected = false,
        onClick = onClick
    )
}

@Composable
private fun RecentChatCard(
    title: String,
    subtitle: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .clip(RoundedCornerShape(24.dp))
            .background(if (selected) Colors.PRIMARY else Colors.WHITE)
            .rippleClickable(onClick = onClick)
            .width(156.dp)
            .padding(14.dp)
    ) {
        Text(
            text = title,
            color = if (selected) Colors.WHITE else Colors.BLUE_TEXT,
            style = Typography.H3,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
        Spacer(Modifier.height(6.dp))
        Text(
            text = subtitle,
            color = if (selected) Colors.WHITE.copy(alpha = 0.8f) else Colors.SECONDARY_TEXT,
            style = Typography.S,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis
        )
    }
}
