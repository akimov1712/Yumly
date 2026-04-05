package ru.topbun.core.ui.components

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.unit.dp
import ru.topbun.core.ui.theme.Colors

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppSlider(
    value: Float,
    onValueChange: (Float) -> Unit,
) {
    Slider(
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
        track = {
            Box(
                Modifier
                    .fillMaxWidth()
                    .animateContentSize()
                    .height(if (it.isDragging) 9.dp else 8.dp)
            ){
                Box(
                    Modifier
                        .fillMaxSize()
                        .clip(CircleShape)
                        .background(Colors.FORM)
                )
                Box(
                    Modifier
                        .fillMaxHeight()
                        .fillMaxWidth(it.value)
                        .clip(CircleShape)
                        .background(Colors.PRIMARY)
                )
            }
        },
        thumb = {
            val animateSizeThumb by animateFloatAsState(if (it.isDragging) 1.25f else 1f)
            Box(
                modifier = Modifier
                    .scale(animateSizeThumb)
                    .size(16.dp)
                    .clip(CircleShape)
                    .background(Colors.PRIMARY)
            )
        },
    )
}