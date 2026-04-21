package ru.topbun.assistant.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.topbun.core.ui.components.Height
import ru.topbun.core.ui.theme.Colors
import ru.topbun.core.ui.theme.Typography
import ru.topbun.core.ui.utils.rippleClickable
import ru.topbun.domain.entity.gpt.GptChatEntity

@Composable
internal fun ChatPreviewItem(
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
