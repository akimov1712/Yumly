package ru.topbun.bmi.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
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

@Composable
internal fun AgeSection(
    age: Int,
    onChange: (Int) -> Unit,
) = MetricSection(
    title = "Возраст",
    valueLabel = buildValueLabel(age.toString(), "лет")
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        AgeStepButton(R.drawable.ic_minus) { onChange(age - 1) }
        Row(modifier = Modifier.weight(1f)) {
            BmiSlider(
                value = age.toFloat(),
                valueRange = BmiViewModel.MIN_AGE.toFloat()..BmiViewModel.MAX_AGE.toFloat(),
                onValueChange = { onChange(it.toInt()) }
            )
        }
        AgeStepButton(R.drawable.ic_plus) { onChange(age + 1) }
    }
}

@Composable
private fun AgeStepButton(iconRes: Int, onClick: () -> Unit) {
    IconButton(
        modifier = Modifier
            .size(40.dp)
            .clip(CircleShape)
            .background(Colors.PRIMARY.copy(alpha = 0.12f)),
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
