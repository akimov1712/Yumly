package ru.topbun.bmi.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.topbun.bmi.BmiViewModel
import ru.topbun.core.ui.components.Height
import ru.topbun.core.ui.theme.Colors
import ru.topbun.core.ui.theme.Fonts
import ru.topbun.core.ui.theme.Typography

@Composable
internal fun HeightSection(
    height: Int,
    onChange: (Int) -> Unit,
) = MetricSection(
    title = "Рост",
    valueLabel = buildValueLabel(height.toString(), "см")
) {
    BmiSlider(
        value = height.toFloat(),
        valueRange = BmiViewModel.MIN_HEIGHT.toFloat()..BmiViewModel.MAX_HEIGHT.toFloat(),
        onValueChange = { onChange(it.toInt()) }
    )
}

@Composable
internal fun BmiSlider(
    value: Float,
    valueRange: ClosedFloatingPointRange<Float>,
    onValueChange: (Float) -> Unit,
) {
    Slider(
        modifier = Modifier.fillMaxWidth(),
        value = value,
        valueRange = valueRange,
        onValueChange = onValueChange,
        colors = SliderDefaults.colors(
            thumbColor = Colors.PRIMARY,
            activeTrackColor = Colors.PRIMARY,
            inactiveTrackColor = Colors.FORM,
        )
    )
}

internal fun buildValueLabel(value: String, unit: String): AnnotatedString = buildAnnotatedString {
    withStyle(SpanStyle(fontSize = 28.sp, fontWeight = FontWeight.Bold)) {
        append(value)
    }
    withStyle(SpanStyle(color = Colors.SECONDARY_TEXT, fontSize = 14.sp)) {
        append("  $unit")
    }
}

@Composable
internal fun MetricSection(
    title: String,
    valueLabel: AnnotatedString,
    content: @Composable () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(28.dp))
            .background(Colors.WHITE)
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = title,
                style = Typography.H2,
                color = Colors.MAIN_TEXT
            )
            Text(
                text = valueLabel,
                color = Colors.PRIMARY,
                fontFamily = Fonts.INTER,
            )
        }
        Height(4.dp)
        content()
    }
}
