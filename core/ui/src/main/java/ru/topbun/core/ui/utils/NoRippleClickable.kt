package ru.topbun.core.ui.utils

import androidx.compose.foundation.clickable
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun Modifier.noRippleClickable(onClick: () -> Unit) = this.clickable(
    interactionSource = null, indication = null, onClick = onClick
)