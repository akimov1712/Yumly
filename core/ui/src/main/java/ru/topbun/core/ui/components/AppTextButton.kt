package ru.topbun.core.ui.components

import android.R.attr.textColor
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import ru.topbun.core.ui.theme.Colors
import ru.topbun.core.ui.theme.Typography

@Composable
fun AppTextButton(
    text: String,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = ButtonDefaults.TextButtonContentPadding,
    style: TextStyle = Typography.H3,
    containerColor: Color = Colors.PRIMARY,
    textColor: Color = Colors.PRIMARY,
    onClick: () -> Unit

) {
    TextButton(
        modifier = modifier,
        onClick = onClick,
        contentPadding = contentPadding,
        colors = ButtonColors(
            containerColor = Color.Transparent,
            contentColor = containerColor,
            disabledContainerColor = Color.Transparent,
            disabledContentColor = Colors.WHITE
        )
    ) {
        Text(
            text = text,
            style = style,
            color = textColor
        )
    }
}