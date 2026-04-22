package ru.topbun.assistant.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import ru.topbun.core.ui.components.AppButton
import ru.topbun.core.ui.components.Height
import ru.topbun.core.ui.theme.Colors
import ru.topbun.core.ui.theme.Typography

@Composable
internal fun ChatLoadError(
    onRetry: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier.fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
                .clip(RoundedCornerShape(28.dp))
                .background(Colors.WHITE)
                .padding(horizontal = 22.dp, vertical = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Не удалось загрузить чат",
                color = Colors.MAIN_TEXT,
                style = Typography.H2,
                textAlign = TextAlign.Center
            )
            Height(8.dp)
            Text(
                text = "Проверьте соединение и попробуйте ещё раз",
                color = Colors.SECONDARY_TEXT,
                style = Typography.P2,
                textAlign = TextAlign.Center
            )
            Height(18.dp)
            AppButton(
                text = "Повторить",
                onClick = onRetry
            )
        }
    }
}
