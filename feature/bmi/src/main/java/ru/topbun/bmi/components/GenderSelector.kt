package ru.topbun.bmi.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import ru.topbun.bmi.BmiState
import ru.topbun.core.ui.R
import ru.topbun.core.ui.components.Height
import ru.topbun.core.ui.theme.Colors
import ru.topbun.core.ui.theme.Typography
import ru.topbun.core.ui.utils.rippleClickable

@Composable
internal fun GenderSelector(
    selected: BmiState.Gender,
    onSelect: (BmiState.Gender) -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(28.dp))
            .background(Colors.WHITE)
            .padding(20.dp),
    ) {
        Text(
            text = "Пол",
            style = Typography.H2,
            color = Colors.MAIN_TEXT
        )
        Height(16.dp)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            BmiState.Gender.entries.forEach { gender ->
                GenderChip(
                    gender = gender,
                    selected = gender == selected,
                    onClick = { onSelect(gender) }
                )
            }
        }
    }
}

@Composable
private fun RowScope.GenderChip(
    gender: BmiState.Gender,
    selected: Boolean,
    onClick: () -> Unit,
) {
    val background by animateColorAsState(
        targetValue = if (selected) Colors.PRIMARY else Colors.FORM,
        animationSpec = tween(220),
        label = "chip_bg"
    )
    val contentColor by animateColorAsState(
        targetValue = if (selected) Colors.WHITE else Colors.MAIN_TEXT,
        animationSpec = tween(220),
        label = "chip_fg"
    )

    Row(
        modifier = Modifier
            .weight(1f)
            .clip(RoundedCornerShape(20.dp))
            .background(background)
            .rippleClickable(color = Colors.WHITE, onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Icon(
            modifier = Modifier
                .size(24.dp)
                .clip(CircleShape),
            painter = painterResource(
                if (gender == BmiState.Gender.Female) R.drawable.ic_female else R.drawable.ic_male
            ),
            contentDescription = null,
            tint = contentColor
        )
        Text(
            text = gender.title,
            style = Typography.P2,
            color = contentColor
        )
    }
}
