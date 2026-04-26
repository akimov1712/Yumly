package ru.topbun.recipe.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import ru.topbun.core.ui.theme.Colors
import ru.topbun.core.ui.theme.Typography
import ru.topbun.core.ui.utils.rippleClickable
import ru.topbun.recipe.RecipeState.IngredientMode

@Composable
internal fun IngredientModeBar(
    selected: IngredientMode,
    onChangeMode: (IngredientMode) -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp)
            .clip(RoundedCornerShape(28.dp))
            .background(Colors.FORM)
            .padding(4.dp),
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        IngredientMode.entries.forEach { mode ->
            ModeTab(
                mode = mode,
                selected = selected == mode,
                onClick = { onChangeMode(mode) }
            )
        }
    }
}

@Composable
private fun RowScope.ModeTab(
    mode: IngredientMode,
    selected: Boolean,
    onClick: () -> Unit,
) {
    val accent = mode.accentColor()
    val backgroundColor by animateColorAsState(
        targetValue = if (selected) accent else Color.Transparent,
        animationSpec = tween(200),
        label = "tab_bg"
    )
    val textColor by animateColorAsState(
        targetValue = if (selected) Colors.WHITE else Colors.SECONDARY_TEXT,
        animationSpec = tween(200),
        label = "tab_text"
    )
    Box(
        modifier = Modifier
            .weight(1f)
            .clip(RoundedCornerShape(24.dp))
            .background(backgroundColor)
            .rippleClickable(color = accent, onClick = onClick)
            .padding(vertical = 10.dp, horizontal = 8.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = mode.title,
            style = Typography.S.copy(fontWeight = FontWeight.Bold),
            color = textColor,
            maxLines = 1
        )
    }
}
