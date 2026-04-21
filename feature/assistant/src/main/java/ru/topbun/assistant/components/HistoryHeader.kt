package ru.topbun.assistant.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ru.topbun.core.ui.components.AppButton
import ru.topbun.core.ui.components.Height
import ru.topbun.core.ui.theme.Colors
import ru.topbun.core.ui.theme.Typography

@Composable
internal fun HistoryHeader(
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
