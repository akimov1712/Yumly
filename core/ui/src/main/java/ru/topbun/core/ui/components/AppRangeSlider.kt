package ru.topbun.core.ui.components

import android.R.attr.maxWidth
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.RangeSlider
import androidx.compose.material3.SliderDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.times
import ru.topbun.core.ui.theme.Colors

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppRangeSlider(
    value: ClosedFloatingPointRange<Float>,
    onValueChange: (ClosedFloatingPointRange<Float>) -> Unit,
) {
    val startInteractionSource = remember { MutableInteractionSource() }
    val endInteractionSource = remember { MutableInteractionSource() }

    val startDragging by startInteractionSource.collectIsPressedAsState()
    val endDragging by endInteractionSource.collectIsPressedAsState()

    RangeSlider(
        value = value,
        onValueChange = onValueChange,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp),
        colors = SliderDefaults.colors(
            thumbColor = Colors.PRIMARY,
            activeTrackColor = Colors.PRIMARY,
            inactiveTrackColor = Colors.FORM,
            activeTickColor = Colors.PRIMARY,
            inactiveTickColor = Colors.FORM
        ),
        startInteractionSource = startInteractionSource,
        endInteractionSource = endInteractionSource,
        track = { sliderState ->
            val startFraction = sliderState.activeRangeStart
            val endFraction = sliderState.activeRangeEnd
            val isDragging = startDragging || endDragging

            val trackHeight by animateDpAsState(
                targetValue = if (isDragging) 9.dp else 8.dp,
                label = ""
            )

            BoxWithConstraints(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(trackHeight)
            ) {
                val widthPx = constraints.maxWidth.toFloat()

                Box(
                    Modifier
                        .fillMaxSize()
                        .clip(CircleShape)
                        .background(Colors.FORM)
                )

                Box(
                    Modifier
                        .offset {
                            IntOffset(
                                x = (startFraction * widthPx).toInt(),
                                y = 0
                            )
                        }
                        .width((endFraction - startFraction) * maxWidth)
                        .fillMaxHeight()
                        .clip(CircleShape)
                        .background(Colors.PRIMARY)
                )
            }
        },
        startThumb = {
            val scale by animateFloatAsState(
                if (startDragging) 1.25f else 1f,
                label = ""
            )

            Box(
                modifier = Modifier
                    .scale(scale)
                    .size(16.dp)
                    .clip(CircleShape)
                    .background(Colors.PRIMARY)
            )
        },
        endThumb = {
            val scale by animateFloatAsState(
                if (endDragging) 1.25f else 1f,
                label = ""
            )

            Box(
                modifier = Modifier
                    .scale(scale)
                    .size(16.dp)
                    .clip(CircleShape)
                    .background(Colors.PRIMARY)
            )
        },
    )
}