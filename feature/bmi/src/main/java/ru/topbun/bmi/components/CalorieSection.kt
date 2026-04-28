package ru.topbun.bmi.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
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

@Composable
internal fun CalorieSection(
    bmr: Int,
    daily: Int,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(28.dp))
            .background(Colors.WHITE)
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = "Калории",
            style = Typography.H2,
            color = Colors.MAIN_TEXT
        )
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            CalorieCard(
                modifier = Modifier.weight(1f),
                iconRes = R.drawable.ic_calories,
                title = "Базовый обмен",
                value = bmr,
                tint = Colors.BLUE_TEXT,
                background = Colors.BLUE_TEXT.copy(alpha = 0.10f)
            )
            CalorieCard(
                modifier = Modifier.weight(1f),
                iconRes = R.drawable.ic_calories,
                title = "В день",
                value = daily,
                tint = Colors.PRIMARY,
                background = Colors.PRIMARY.copy(alpha = 0.10f)
            )
        }
        Text(
            text = "Расчёт по формуле Миффлина-Сан Жеора с малой активностью",
            style = Typography.S,
            color = Colors.SECONDARY_TEXT,
        )
    }
}

@Composable
private fun CalorieCard(
    modifier: Modifier,
    iconRes: Int,
    title: String,
    value: Int,
    tint: androidx.compose.ui.graphics.Color,
    background: androidx.compose.ui.graphics.Color,
) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(20.dp))
            .background(background)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Box(
            modifier = Modifier
                .size(36.dp)
                .clip(CircleShape)
                .background(tint.copy(alpha = 0.18f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                modifier = Modifier.size(18.dp),
                painter = painterResource(iconRes),
                contentDescription = null,
                tint = tint
            )
        }
        Height(2.dp)
        Text(
            text = title,
            style = Typography.S,
            color = Colors.SECONDARY_TEXT
        )
        Text(
            text = "$value ккал",
            style = Typography.H2,
            color = Colors.MAIN_TEXT
        )
    }
}
