package ru.topbun.assistant.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ru.topbun.core.ui.components.AppButton
import ru.topbun.core.ui.components.Height
import ru.topbun.core.ui.theme.Colors
import ru.topbun.core.ui.theme.Typography

@Composable
internal fun DrawerHeader(
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
