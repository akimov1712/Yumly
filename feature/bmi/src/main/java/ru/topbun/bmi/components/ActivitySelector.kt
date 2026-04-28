package ru.topbun.bmi.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.topbun.bmi.BmiState
import ru.topbun.core.ui.components.Height
import ru.topbun.core.ui.theme.Colors
import ru.topbun.core.ui.theme.Fonts
import ru.topbun.core.ui.theme.Typography
import ru.topbun.core.ui.utils.rippleClickable

@Composable
internal fun ActivitySelector(
    selected: BmiState.Activity,
    onSelect: (BmiState.Activity) -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(28.dp))
            .background(Colors.WHITE)
            .padding(20.dp),
    ) {
        Text(
            text = "Физическая активность",
            style = Typography.H2,
            color = Colors.MAIN_TEXT
        )
        Height(4.dp)
        Text(
            text = "Норма калорий зависит от уровня нагрузки",
            style = Typography.S,
            color = Colors.SECONDARY_TEXT,
        )
        Height(16.dp)
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            BmiState.Activity.entries.forEach { activity ->
                ActivityRow(
                    activity = activity,
                    selected = activity == selected,
                    onClick = { onSelect(activity) }
                )
            }
        }
    }
}

@Composable
private fun ActivityRow(
    activity: BmiState.Activity,
    selected: Boolean,
    onClick: () -> Unit,
) {
    val background by animateColorAsState(
        targetValue = if (selected) Colors.PRIMARY.copy(alpha = 0.08f) else Colors.FORM,
        animationSpec = tween(200),
        label = "activity_bg"
    )
    val borderColor by animateColorAsState(
        targetValue = if (selected) Colors.PRIMARY else Colors.OUTLINE.copy(alpha = 0.5f),
        animationSpec = tween(200),
        label = "activity_border"
    )

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(background)
            .border(
                width = if (selected) 2.dp else 1.dp,
                color = borderColor,
                shape = RoundedCornerShape(20.dp),
            )
            .rippleClickable(color = Colors.PRIMARY, onClick = onClick)
            .padding(horizontal = 14.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        SelectIndicator(selected = selected)
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = activity.title,
                style = Typography.P2,
                color = Colors.MAIN_TEXT,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = activity.description,
                style = Typography.S,
                color = Colors.SECONDARY_TEXT,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
        }
        Text(
            text = "×${activity.factor}",
            color = if (selected) Colors.PRIMARY else Colors.SECONDARY_TEXT,
            fontFamily = Fonts.INTER,
            fontWeight = FontWeight.Bold,
            fontSize = 14.sp,
        )
    }
}

@Composable
private fun SelectIndicator(selected: Boolean) {
    val borderColor by animateColorAsState(
        targetValue = if (selected) Colors.PRIMARY else Colors.OUTLINE,
        animationSpec = tween(200),
        label = "indicator_border"
    )
    Box(
        modifier = Modifier
            .size(22.dp)
            .clip(CircleShape)
            .border(2.dp, borderColor, CircleShape)
            .background(Colors.WHITE),
        contentAlignment = Alignment.Center
    ) {
        if (selected) {
            Box(
                modifier = Modifier
                    .size(10.dp)
                    .clip(CircleShape)
                    .background(Colors.PRIMARY)
            )
        }
    }
}
