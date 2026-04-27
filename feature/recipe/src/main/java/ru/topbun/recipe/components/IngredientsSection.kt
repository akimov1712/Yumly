package ru.topbun.recipe.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import ru.topbun.core.ui.R
import ru.topbun.core.ui.components.AppTextButton
import ru.topbun.core.ui.components.Height
import ru.topbun.core.ui.theme.Colors
import ru.topbun.core.ui.theme.Typography
import ru.topbun.core.ui.utils.rippleClickable
import ru.topbun.domain.entity.recipe.IngredientEntity

@Composable
internal fun IngredientsSection(
    ingredients: List<IngredientEntity>,
    checkedIndices: Set<Int>,
    progress: Float,
    onToggleIngredient: (index: Int) -> Unit,
    onClickReset: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(28.dp))
            .background(Colors.WHITE)
            .padding(top = 10.dp, bottom = 20.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .defaultMinSize(minHeight = 48.dp)
                .padding(horizontal = 20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                modifier = Modifier.weight(1f),
                text = buildAnnotatedString {
                    append("Ингредиенты ")
                    withStyle(SpanStyle(color = Colors.SECONDARY_TEXT)) {
                        append("(${ingredients.size})")
                    }
                },
                style = Typography.H2,
                color = Colors.MAIN_TEXT
            )
            if (checkedIndices.isNotEmpty()) {
                AppTextButton(
                    modifier = Modifier.defaultMinSize(minHeight = 48.dp),
                    text = "Сбросить",
                    containerColor = Colors.SECONDARY_TEXT,
                    textColor = Colors.SECONDARY_TEXT,
                    onClick = onClickReset
                )
            }
        }
        Height(14.dp)
        ProgressBlock(
            progress = progress,
            checkedCount = checkedIndices.size,
            total = ingredients.size,
        )
        Height(8.dp)
        Column(
            modifier = Modifier.padding(horizontal = 12.dp)
        ) {
            ingredients.forEachIndexed { index, ingredient ->
                IngredientRow(
                    ingredient = ingredient,
                    checked = checkedIndices.contains(index),
                    onToggle = { onToggleIngredient(index) }
                )
            }
        }
    }
}

@Composable
private fun ProgressBlock(
    progress: Float,
    checkedCount: Int,
    total: Int,
) {
    val animatedProgress by animateFloatAsState(
        targetValue = progress.coerceIn(0f, 1f),
        animationSpec = tween(400),
        label = "ingredient_progress"
    )
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp),
        verticalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                modifier = Modifier.weight(1f),
                text = "У меня есть дома",
                style = Typography.S,
                color = Colors.SECONDARY_TEXT
            )
            Text(
                text = buildAnnotatedString {
                    withStyle(SpanStyle(color = Colors.PRIMARY)) {
                        append("$checkedCount")
                    }
                    append(" из $total")
                },
                style = Typography.S,
                color = Colors.SECONDARY_TEXT
            )
        }
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(8.dp)
                .clip(RoundedCornerShape(4.dp))
                .background(Colors.PRIMARY.copy(0.15f))
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(animatedProgress)
                    .height(8.dp)
                    .clip(RoundedCornerShape(4.dp))
                    .background(Colors.PRIMARY)
            )
        }
    }
}

@Composable
private fun IngredientRow(
    ingredient: IngredientEntity,
    checked: Boolean,
    onToggle: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .rippleClickable(color = Colors.PRIMARY, onClick = onToggle)
            .padding(horizontal = 8.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        CheckMark(checked = checked)
        Column(
            modifier = Modifier
                .weight(1f)
                .alpha(if (checked) 0.55f else 1f)
        ) {
            Text(
                text = ingredient.name,
                style = Typography.P1.copy(
                    textDecoration = if (checked) TextDecoration.LineThrough else TextDecoration.None
                ),
                color = Colors.MAIN_TEXT
            )
        }
        Text(
            modifier = Modifier.alpha(if (checked) 0.55f else 1f),
            text = ingredient.value,
            style = Typography.S,
            color = Colors.SECONDARY_TEXT
        )
    }
}

@Composable
private fun CheckMark(checked: Boolean) {
    Box(
        modifier = Modifier
            .size(24.dp)
            .clip(CircleShape)
            .background(if (checked) Colors.PRIMARY else Colors.PRIMARY.copy(0.12f)),
        contentAlignment = Alignment.Center
    ) {
        if (checked) {
            Icon(
                modifier = Modifier.size(10.dp),
                painter = painterResource(R.drawable.ic_checkmark),
                contentDescription = null,
                tint = Colors.WHITE
            )
        }
    }
}
