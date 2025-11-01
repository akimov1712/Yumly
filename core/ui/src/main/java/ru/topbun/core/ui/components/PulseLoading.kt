package ru.topbun.core.ui.components

import androidx.compose.animation.core.InfiniteRepeatableSpec
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ru.topbun.core.ui.theme.Colors

@Composable
fun PulseLoading() {
    val infiniteTransition = rememberInfiniteTransition()
    val animatePulseColor by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 84f,
        animationSpec = InfiniteRepeatableSpec(
            animation = tween(durationMillis = 1000, easing = LinearEasing),
        )
    )

    Box(contentAlignment = Alignment.Center) {
        Box(
            Modifier
                .size(48.dp)
                .background(Colors.WHITE, CircleShape)
        )
        Box(
            Modifier
                .size(animatePulseColor.dp)
                .background(Colors.WHITE.copy(0.5f), CircleShape)
        )
    }
}
