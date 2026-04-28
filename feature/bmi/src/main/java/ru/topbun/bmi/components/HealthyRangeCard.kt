package ru.topbun.bmi.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import ru.topbun.core.ui.R
import ru.topbun.core.ui.components.Height
import ru.topbun.core.ui.theme.Colors
import ru.topbun.core.ui.theme.Typography
import kotlin.math.roundToInt

@Composable
internal fun HealthyRangeCard(
    minKg: Float,
    maxKg: Float,
    currentKg: Float,
) {
    val rangeText = "${minKg.roundToInt()} – ${maxKg.roundToInt()} кг"
    val tip = when {
        currentKg < minKg -> "Чтобы попасть в норму, добавьте ${(minKg - currentKg).roundToInt()} кг"
        currentKg > maxKg -> "Для нормы можно скинуть ${(currentKg - maxKg).roundToInt()} кг"
        else -> "Ваш текущий вес в пределах нормы"
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(28.dp))
            .background(Colors.WHITE)
            .padding(20.dp),
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .background(Colors.PRIMARY.copy(alpha = 0.14f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    modifier = Modifier.size(18.dp),
                    painter = painterResource(R.drawable.ic_checkmark),
                    contentDescription = null,
                    tint = Colors.PRIMARY
                )
            }
            Column {
                Text(
                    text = "Здоровый диапазон веса",
                    style = Typography.S,
                    color = Colors.SECONDARY_TEXT
                )
                Text(
                    text = rangeText,
                    style = Typography.H3,
                    color = Colors.MAIN_TEXT
                )
            }
        }
        Height(12.dp)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(1.dp)
                .background(Colors.OUTLINE)
        )
        Height(12.dp)
        Text(
            text = tip,
            style = Typography.P2,
            color = Colors.MAIN_TEXT,
        )
    }
}
