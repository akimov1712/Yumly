package ru.topbun.core.ui.components

import androidx.compose.animation.animateColor
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.InfiniteRepeatableSpec
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.StartOffset
import androidx.compose.animation.core.StartOffsetType
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
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import ru.topbun.core.ui.theme.Colors

@Composable
fun PulseLoading(
    color: Color = Colors.WHITE,
    durationMillis: Int = 1500,
    countCircles: Int = 2,
    countScale: Float = 4f,
    initialSizeShape: Dp = 28.dp,
    shape: Shape = CircleShape
) {
    val infiniteTransition = rememberInfiniteTransition()

    Box(contentAlignment = Alignment.Center) {
        repeat(countCircles){

            val offsetMillis = durationMillis - it * (durationMillis / countCircles)

            val animateScale by infiniteTransition.animateFloat(
                initialValue = 1f,
                targetValue = countScale,
                animationSpec = InfiniteRepeatableSpec(
                    animation = tween(durationMillis = durationMillis, easing = LinearEasing),
                    initialStartOffset = StartOffset(offsetMillis, StartOffsetType.Delay)
                )
            )

            val animateColor by infiniteTransition.animateColor(
                initialValue = color,
                targetValue = color.copy(0f),
                animationSpec = InfiniteRepeatableSpec(
                    animation = tween(durationMillis = durationMillis, easing = LinearEasing),
                    initialStartOffset = StartOffset(offsetMillis, StartOffsetType.Delay)
                )
            )

            Box(
                Modifier
                    .size(initialSizeShape)
                    .scale(animateScale)
                    .background(animateColor, shape)
            )

        }
    }
}
