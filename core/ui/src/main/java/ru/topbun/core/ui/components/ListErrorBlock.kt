package ru.topbun.core.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import ru.topbun.core.ui.theme.Colors
import ru.topbun.core.ui.theme.Typography

@Composable
fun ListErrorBlock(
    modifier: Modifier = Modifier,
    title: String = "Не удалось загрузить",
    message: String = "Проверьте подключение к интернету и попробуйте ещё раз",
    iconRes: Int = R.drawable.ic_internet,
    accent: androidx.compose.ui.graphics.Color = Colors.SECONDARY,
    showRetry: Boolean = true,
    onClickRetry: () -> Unit = {},
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(28.dp))
            .background(Colors.WHITE)
            .padding(horizontal = 24.dp, vertical = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box(
            modifier = Modifier
                .size(72.dp)
                .clip(CircleShape)
                .background(accent.copy(alpha = 0.12f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                modifier = Modifier.size(32.dp),
                painter = painterResource(iconRes),
                contentDescription = null,
                tint = accent
            )
        }
        Height(16.dp)
        Text(
            text = title,
            style = Typography.H2,
            color = Colors.MAIN_TEXT,
            textAlign = TextAlign.Center
        )
        Height(8.dp)
        Text(
            text = message,
            style = Typography.P2,
            color = Colors.SECONDARY_TEXT,
            textAlign = TextAlign.Center
        )
        if (showRetry) {
            Height(20.dp)
            AppButton(
                text = "Повторить",
                modifier = Modifier.fillMaxWidth(),
                onClick = onClickRetry
            )
        }
    }
}
