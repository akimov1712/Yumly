package ru.topbun.core.ui.components

import android.R.attr.enabled
import android.R.attr.onClick
import android.R.attr.text
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.topbun.core.ui.theme.Colors
import ru.topbun.core.ui.theme.Fonts

@Composable
fun AppOutlinedButton(
    text: String,
    modifier: Modifier,
    enabled: Boolean = true,
    borderColor: Color = if (enabled) Colors.PRIMARY else Colors.OUTLINE,
    contentColor: Color = if (enabled) Colors.PRIMARY else Colors.OUTLINE,
    startIcon: @Composable (() -> Unit)? = null,
    isLoading: Boolean = false,
    onClick: () -> Unit
) {
    val enabled = enabled && !isLoading
    OutlinedButton(
        modifier = modifier,
        shape = RoundedCornerShape(32.dp),
        onClick = onClick,
        enabled = enabled,
        border = BorderStroke(2.dp, borderColor)
    ) {
        if (isLoading){
            CircularProgressIndicator(
                modifier = Modifier.size(20.dp),
                color = contentColor,
                strokeWidth = 2.5.dp
            )
        } else {
            Row(
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                verticalAlignment = Alignment.CenterVertically
            ){
                startIcon?.let { it() }
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
}
