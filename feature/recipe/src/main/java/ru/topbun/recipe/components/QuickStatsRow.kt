package ru.topbun.recipe.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import ru.topbun.core.ui.R
import ru.topbun.core.ui.components.Height
import ru.topbun.core.ui.theme.Colors
import ru.topbun.core.ui.theme.Typography
import ru.topbun.core.ui.utils.formatCookingTime
import ru.topbun.core.ui.utils.formatRecipeDifficulty
import ru.topbun.domain.entity.recipe.RecipeDifficulty
import ru.topbun.domain.entity.recipe.RecipeEntity

@Composable
internal fun QuickStatsRow(
    recipe: RecipeEntity,
    modifier: Modifier = Modifier,
) {
    val difficultyColor = when (recipe.difficulty) {
        RecipeDifficulty.Easy -> Colors.GREEN
        RecipeDifficulty.Normal -> Colors.ORANGE
        RecipeDifficulty.Hard -> Colors.RED
    }
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(28.dp))
            .background(Colors.WHITE)
            .padding(vertical = 18.dp, horizontal = 12.dp),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        StatBlock(
            iconRes = R.drawable.ic_time,
            iconTint = Colors.PRIMARY,
            value = formatCookingTime(recipe.cookingTime),
            label = "Время"
        )
        StatDivider()
        StatBlock(
            iconRes = when (recipe.difficulty) {
                RecipeDifficulty.Easy -> R.drawable.ic_difficulty_easy
                RecipeDifficulty.Normal -> R.drawable.ic_difficulty_normal
                RecipeDifficulty.Hard -> R.drawable.ic_difficulty_hard
            },
            iconTint = difficultyColor,
            value = formatRecipeDifficulty(recipe.difficulty),
            label = "Сложность"
        )
        StatDivider()
        StatBlock(
            iconRes = R.drawable.ic_calories,
            iconTint = Colors.SECONDARY,
            value = "${recipe.kcal}",
            label = "ккал"
        )
    }
}

@Composable
private fun RowScope.StatBlock(
    iconRes: Int,
    iconTint: Color,
    value: String,
    label: String,
) {
    Column(
        modifier = Modifier.weight(1f),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .background(iconTint.copy(0.12f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                modifier = Modifier.size(20.dp),
                painter = painterResource(iconRes),
                contentDescription = null,
                tint = iconTint
            )
        }
        Height(8.dp)
        Text(
            text = value,
            style = Typography.H3,
            color = Colors.MAIN_TEXT,
            textAlign = TextAlign.Center
        )
        Text(
            text = label,
            style = Typography.S,
            color = Colors.SECONDARY_TEXT,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
private fun StatDivider() {
    Box(
        modifier = Modifier
            .size(1.dp, 56.dp)
            .background(Colors.OUTLINE.copy(0.5f))
    )
}
