package ru.topbun.auth_login.components

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.style.TextAlign
import ru.topbun.core.ui.theme.Colors
import ru.topbun.core.ui.theme.Typography

@Composable
internal fun Description() {
    Text(
        text = "Введите данные своего аккаунта",
        color = Colors.SECONDARY_TEXT,
        style = Typography.P2,
        textAlign = TextAlign.Center
    )
}

