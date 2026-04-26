package ru.topbun.recipe.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import ru.topbun.core.ui.theme.Colors
import ru.topbun.core.ui.theme.Typography
import ru.topbun.domain.entity.recipe.RecipeEntity

@OptIn(ExperimentalLayoutApi::class)
@Composable
internal fun TagsRow(
    recipe: RecipeEntity,
    modifier: Modifier = Modifier,
) {
    if (recipe.tags.isEmpty()) return

    FlowRow(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        recipe.tags.forEach { tag ->
            Text(
                modifier = Modifier
                    .clip(RoundedCornerShape(20.dp))
                    .background(Colors.PRIMARY.copy(0.12f))
                    .padding(horizontal = 14.dp, vertical = 8.dp),
                text = tag.name,
                style = Typography.S,
                color = Colors.PRIMARY
            )
        }
    }
}
