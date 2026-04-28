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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import ru.topbun.bmi.BmiState
import ru.topbun.core.ui.R
import ru.topbun.core.ui.components.Height
import ru.topbun.core.ui.theme.Colors
import ru.topbun.core.ui.theme.Typography

@Composable
internal fun CalorieSection(
    bmr: Int,
    daily: Int,
    activity: BmiState.Activity,
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
                title = "Покой (BMR)",
                hint = "Минимум для жизни",
                value = bmr,
                tint = Colors.BLUE_TEXT,
                background = Colors.BLUE_TEXT.copy(alpha = 0.10f)
            )
            CalorieCard(
                modifier = Modifier.weight(1f),
                iconRes = R.drawable.ic_calories,
                title = "Норма в день",
                hint = "С учётом активности",
                value = daily,
                tint = Colors.PRIMARY,
                background = Colors.PRIMARY.copy(alpha = 0.10f)
            )
        }
        ExplanationRow()
        Text(
            text = "Расчёт: BMR × ${activity.factor} (${activity.title.lowercase()}). " +
                    "Чтобы похудеть — отнимите 10–20% от нормы, чтобы набрать массу — добавьте столько же.",
            style = Typography.S,
            color = Colors.SECONDARY_TEXT,
        )
    }
}

@Composable
private fun ExplanationRow() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(Colors.FORM)
            .padding(horizontal = 14.dp, vertical = 12.dp),
        verticalAlignment = Alignment.Top,
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Box(
            modifier = Modifier
                .size(28.dp)
                .clip(CircleShape)
                .background(Colors.PRIMARY.copy(alpha = 0.18f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                modifier = Modifier.size(20.dp),
                painter = painterResource(R.drawable.ic_lamp),
                contentDescription = null,
                tint = Colors.PRIMARY
            )
        }
        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
            Text(
                text = "Покой — энергия только на дыхание, сердце и обмен веществ. " +
                        "Норма в день — то, что нужно съедать с учётом всей вашей активности.",
                style = Typography.S,
                color = Colors.MAIN_TEXT,
            )
        }
    }
}

@Composable
private fun CalorieCard(
    modifier: Modifier,
    iconRes: Int,
    title: String,
    hint: String,
    value: Int,
    tint: Color,
    background: Color,
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
        Text(
            text = hint,
            style = Typography.S,
            color = Colors.SECONDARY_TEXT
        )
    }
}
