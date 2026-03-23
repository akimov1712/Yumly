package ru.topbun.core.ui.components

import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.topbun.core.ui.theme.Colors
import ru.topbun.core.ui.theme.Fonts

@Composable
fun AppButton(
    text: String,
    modifier: Modifier,
    onClick: () -> Unit
) {
    Button(
        modifier = modifier,
        shape = RoundedCornerShape(32.dp),
        onClick = onClick,
        colors = ButtonColors(
            containerColor = Colors.PRIMARY,
            contentColor = Colors.WHITE,
            disabledContainerColor = Colors.PRIMARY.copy(0.7f),
            disabledContentColor = Colors.WHITE
        ),
    ) {
        Text(
            text = text,
            color = Colors.WHITE,
            fontFamily = Fonts.INTER,
            fontWeight = FontWeight.Bold,
            fontSize = 15.sp,
        )
    }
}