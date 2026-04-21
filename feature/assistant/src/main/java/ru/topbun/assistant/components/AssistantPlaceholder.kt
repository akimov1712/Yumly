package ru.topbun.assistant.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import ru.topbun.core.ui.R
import ru.topbun.core.ui.components.Height
import ru.topbun.core.ui.theme.Colors
import ru.topbun.core.ui.theme.Typography
import ru.topbun.core.ui.utils.rippleClickable

@Composable
internal fun AssistantPlaceholder(
    modifier: Modifier = Modifier,
    hasChats: Boolean,
    onOpenHistory: () -> Unit
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
                .size(188.dp)
                .clip(RoundedCornerShape(52.dp))
                .background(Colors.WHITE),
            contentAlignment = Alignment.Center
        ) {
            Box(
                modifier = Modifier
                    .size(118.dp)
                    .clip(CircleShape)
                    .background(Colors.PRIMARY.copy(alpha = 0.12f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    modifier = Modifier.size(68.dp),
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
        Height(24.dp)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(28.dp))
                .background(Colors.WHITE)
                .rippleClickable(
                    enabled = hasChats,
                    onClick = onOpenHistory
                )
                .padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                modifier = Modifier.size(28.dp),
                painter = painterResource(R.drawable.ic_plus),
                contentDescription = null,
                tint = Colors.PRIMARY
            )
            Text(
                modifier = Modifier.weight(1f),
                text = if (hasChats) {
                    "Выберите недавний чат сверху или откройте всю историю."
                } else {
                    "Напишите первый вопрос снизу, и ассистент создаст новый чат."
                },
                color = Colors.SECONDARY_TEXT,
                style = Typography.S,
            )
        }
    }
}
