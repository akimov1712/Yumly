package ru.topbun.core.ui.utils

import android.R.attr.onClick
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role
import ru.topbun.core.ui.theme.Colors

@Composable
fun Modifier.noRippleClickable(onClick: () -> Unit) = this.clickable(
    interactionSource = null, indication = null, onClick = onClick
)

@Composable
fun Modifier.rippleClickable(color: Color = Colors.PRIMARY, onClick: () -> Unit): Modifier {
    val interactionSource = remember { MutableInteractionSource() }
    return this.clickable(
        interactionSource = interactionSource,
        indication = ripple(color = color),
        role = Role.Button,
        onClick = onClick
    )
}