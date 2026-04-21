package ru.topbun.assistant.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.ui.unit.dp
import ru.topbun.core.ui.components.Height
import ru.topbun.core.ui.theme.Colors
import ru.topbun.core.ui.theme.Typography
import ru.topbun.domain.entity.gpt.GptMessageEntity
import ru.topbun.domain.entity.gpt.GptMessageRoleType

@Composable
internal fun MessageBubble(message: GptMessageEntity) {
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
                        topStart = 26.dp,
                        topEnd = 26.dp,
                        bottomStart = if (isUser) 26.dp else 10.dp,
                        bottomEnd = if (isUser) 10.dp else 26.dp
                    )
                )
                .background(if (isUser) Colors.PRIMARY else Colors.WHITE)
                .padding(horizontal = 18.dp, vertical = 14.dp)
        ) {
            FormattedMessageText(
                text = message.text,
                color = if (isUser) Colors.WHITE else Colors.MAIN_TEXT
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
