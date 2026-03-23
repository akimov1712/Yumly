package ru.topbun.core.ui.components

import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import ru.topbun.core.ui.theme.Colors
import ru.topbun.core.ui.theme.Typography

@Composable
fun AppTextButton(
    text: String,
    modifier: Modifier,
    onClick: () -> Unit
) {
    TextButton(
        modifier = modifier,
        onClick = onClick,
        colors = ButtonColors(
            containerColor = Color.Transparent,
            contentColor = Colors.PRIMARY,
            disabledContainerColor = Color.Transparent,
            disabledContentColor = Colors.WHITE
        )
    ) {
        Text(
            text = text,
            style = Typography.H3,
            color = Colors.PRIMARY
        )
    }
}