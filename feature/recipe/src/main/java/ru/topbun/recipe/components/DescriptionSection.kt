package ru.topbun.recipe.components

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import ru.topbun.core.ui.R
import ru.topbun.core.ui.components.Width
import ru.topbun.core.ui.theme.Colors
import ru.topbun.core.ui.theme.Typography
import ru.topbun.core.ui.utils.rippleClickable
import ru.topbun.domain.entity.recipe.RecipeEntity

private const val COLLAPSED_MAX_LINES = 4

@Composable
internal fun DescriptionSection(
    recipe: RecipeEntity,
    modifier: Modifier = Modifier,
) {
    val description = recipe.description?.takeIf { it.isNotBlank() }
    val tags = recipe.tags

    if (description == null && tags.isEmpty()) return

    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(28.dp))
            .background(Colors.WHITE)
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "О рецепте",
            style = Typography.H2,
            color = Colors.MAIN_TEXT
        )
        if (description != null) {
            ExpandableText(text = description)
        }
        if (tags.isNotEmpty()) {
            TagsRow(recipe = recipe)
        }
    }
}

@Composable
private fun ExpandableText(text: String) {
    var expanded by remember { mutableStateOf(false) }
    var isExpandable by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .animateContentSize(),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Text(
            modifier = Modifier.fillMaxWidth(),
            text = text,
            style = Typography.P2,
            color = Colors.BLUE_TEXT,
            maxLines = if (expanded) Int.MAX_VALUE else COLLAPSED_MAX_LINES,
            overflow = TextOverflow.Ellipsis,
            onTextLayout = { result ->
                if (!expanded && !isExpandable && result.hasVisualOverflow) {
                    isExpandable = true
                }
            }
        )
        if (isExpandable) {
            ExpandToggle(
                expanded = expanded,
                onClick = { expanded = !expanded }
            )
        }
    }
}

@Composable
private fun ExpandToggle(
    expanded: Boolean,
    onClick: () -> Unit,
) {
    androidx.compose.foundation.layout.Row(
        modifier = Modifier
            .clip(RoundedCornerShape(20.dp))
            .rippleClickable(color = Colors.PRIMARY, onClick = onClick)
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = if (expanded) "Свернуть" else "Читать дальше",
            style = Typography.S,
            color = Colors.PRIMARY
        )
        Width(6.dp)
        Icon(
            modifier = Modifier.size(14.dp),
            painter = painterResource(
                if (expanded) R.drawable.ic_chevron_up else R.drawable.ic_chevron_down
            ),
            contentDescription = null,
            tint = Colors.PRIMARY
        )
    }
}
