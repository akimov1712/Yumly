package ru.topbun.recipe.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
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

@Composable
internal fun DescriptionSection(
    recipe: RecipeEntity,
    modifier: Modifier = Modifier,
) {
    if (recipe.description.isNullOrBlank() && recipe.tags.isEmpty()) return

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
        if (!recipe.description.isNullOrBlank()) {
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = recipe.description.orEmpty(),
                style = Typography.P2,
                color = Colors.BLUE_TEXT
            )
        }
        if (recipe.tags.isNotEmpty()) {
            TagsRow(recipe = recipe)
        }
    }
}
