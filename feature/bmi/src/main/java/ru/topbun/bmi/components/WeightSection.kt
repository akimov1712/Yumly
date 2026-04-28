package ru.topbun.bmi.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import ru.topbun.bmi.BmiViewModel
import ru.topbun.core.ui.R
import ru.topbun.core.ui.theme.Colors
import kotlin.math.roundToInt

@Composable
internal fun WeightSection(
    weight: Float,
    onChange: (Float) -> Unit,
) = MetricSection(
    title = "Вес",
    valueLabel = buildValueLabel(((weight * 10f).roundToInt() / 10f).toString(), "кг")
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        StepButton(
            iconRes = R.drawable.ic_minus,
            onClick = { onChange(weight - 0.5f) }
        )
        Row(modifier = Modifier.weight(1f)) {
            BmiSlider(
                value = weight,
                valueRange = BmiViewModel.MIN_WEIGHT..BmiViewModel.MAX_WEIGHT,
                onValueChange = onChange
            )
        }
        StepButton(
            iconRes = R.drawable.ic_plus,
            onClick = { onChange(weight + 0.5f) }
        )
    }
}

@Composable
private fun StepButton(
    iconRes: Int,
    onClick: () -> Unit,
) {
    IconButton(
        modifier = Modifier
            .size(40.dp)
            .clip(CircleShape)
            .background(Colors.PRIMARY.copy(alpha = 0.12f))
            .padding(2.dp),
        onClick = onClick
    ) {
        Icon(
            modifier = Modifier.size(18.dp),
            painter = painterResource(iconRes),
            contentDescription = null,
            tint = Colors.PRIMARY
        )
    }
}
