package ru.topbun.core.ui.components

import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.topbun.core.ui.theme.Colors
import ru.topbun.core.ui.theme.Fonts

@Composable
fun AppButton(
    text: String,
    modifier: Modifier,
    enabled: Boolean = true,
    isLoading: Boolean = false,
    containerColor: Color = Colors.PRIMARY,
    contentColor: Color = Colors.WHITE,
    onClick: () -> Unit
) {
    Button(
        modifier = modifier,
        shape = RoundedCornerShape(32.dp),
        onClick = onClick,
        enabled = enabled && !isLoading,
        colors = ButtonColors(
            containerColor = containerColor,
            contentColor = contentColor,
            disabledContainerColor = containerColor.copy(0.7f),
            disabledContentColor = contentColor
        ),
    ) {
        if (isLoading){
            CircularProgressIndicator(
                modifier = Modifier.size(20.dp),
                color = contentColor,
                strokeWidth = 2.5.dp
            )
        } else {
            Text(
                text = text,
                color = contentColor,
                fontFamily = Fonts.INTER,
                fontWeight = FontWeight.Bold,
                fontSize = 15.sp,
            )
        }
    }
}