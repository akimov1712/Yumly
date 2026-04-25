package ru.topbun.profile.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import ru.topbun.core.ui.theme.Colors
import ru.topbun.core.ui.theme.Typography
import ru.topbun.core.ui.utils.noRippleClickable

@Composable
internal fun StatItem(
    value: Int,
    label: String,
    onClick: (() -> Unit)? = null,
) {
    val clickableModifier = if (onClick != null) {
        Modifier.noRippleClickable(onClick)
    } else Modifier

    Column(
        modifier = Modifier
            .clip(RoundedCornerShape(12.dp))
            .then(clickableModifier)
            .padding(horizontal = 12.dp, vertical = 4.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = formatStatNumber(value),
            style = Typography.H1,
            color = Colors.MAIN_TEXT
        )
        Text(
            text = label,
            style = Typography.S,
            color = Colors.SECONDARY_TEXT
        )
    }
}

private fun formatStatNumber(value: Int): String = when {
    value >= 1_000_000 -> "${value / 1_000_000}.${(value / 100_000) % 10}M"
    value >= 1_000 -> "${value / 1_000}.${(value / 100) % 10}K"
    else -> value.toString()
}
