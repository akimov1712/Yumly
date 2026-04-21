package ru.topbun.assistant.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import ru.topbun.core.ui.theme.Colors
import ru.topbun.core.ui.theme.Typography
import ru.topbun.core.ui.utils.LocalBottomBarPadding

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
