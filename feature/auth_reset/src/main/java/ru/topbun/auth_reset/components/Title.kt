package ru.topbun.auth_reset.components

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.style.TextAlign
import ru.topbun.core.ui.theme.Colors
import ru.topbun.core.ui.theme.Typography

@Composable
internal fun Title() {
    Text(
        text = "Reset your password",
        color = Colors.MAIN_TEXT,
        style = Typography.H1,
        textAlign = TextAlign.Center
    )
}