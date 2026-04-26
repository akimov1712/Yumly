package ru.topbun.recipe.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
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
import androidx.compose.ui.graphics.Color
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
import ru.topbun.recipe.RecipeState
import ru.topbun.recipe.RecipeState.IngredientMode

@Composable
internal fun IngredientsSection(
    ingredients: List<IngredientEntity>,
    mode: IngredientMode,
    checkedIndices: Set<Int>,
    progress: Float,
    onChangeMode: (IngredientMode) -> Unit,
    onToggleIngredient: (index: Int) -> Unit,
    onClickReset: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val accent = mode.accentColor()
    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(28.dp))
            .background(Colors.WHITE)
            .padding(top = 20.dp, bottom = 20.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
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
                    text = "Сбросить",
                    containerColor = Colors.SECONDARY_TEXT,
                    textColor = Colors.SECONDARY_TEXT,
                    onClick = onClickReset
                )
            }
        }
        Height(14.dp)
        IngredientModeBar(
            selected = mode,
            onChangeMode = onChangeMode
        )
        Height(14.dp)
        ProgressBlock(
            color = accent,
            progress = progress,
            checkedCount = checkedIndices.size,
            total = ingredients.size,
            mode = mode
        )
        Height(8.dp)
        Column(
            modifier = Modifier.padding(horizontal = 12.dp)
        ) {
            ingredients.forEachIndexed { index, ingredient ->
                IngredientRow(
                    ingredient = ingredient,
                    checked = checkedIndices.contains(index),
                    accent = accent,
                    onToggle = { onToggleIngredient(index) }
                )
            }
        }
    }
}

@Composable
private fun ProgressBlock(
    color: Color,
    progress: Float,
    checkedCount: Int,
    total: Int,
    mode: IngredientMode,
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
            AnimatedContent(
                targetState = mode,
                transitionSpec = {
                    fadeIn(tween(180)) togetherWith fadeOut(tween(180))
                },
                label = "mode_caption"
            ) { current ->
                Text(
                    text = current.captionVerb(),
                    style = Typography.S,
                    color = Colors.SECONDARY_TEXT
                )
            }
            Box(modifier = Modifier.weight(1f))
            Text(
                text = buildAnnotatedString {
                    withStyle(SpanStyle(color = color)) {
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
                .background(color.copy(0.15f))
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(animatedProgress)
                    .height(8.dp)
                    .clip(RoundedCornerShape(4.dp))
                    .background(color)
            )
        }
    }
}

@Composable
private fun IngredientRow(
    ingredient: IngredientEntity,
    checked: Boolean,
    accent: Color,
    onToggle: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .rippleClickable(color = accent, onClick = onToggle)
            .padding(horizontal = 8.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        CheckMark(
            checked = checked,
            accent = accent
        )
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
private fun CheckMark(
    checked: Boolean,
    accent: Color,
) {
    Box(
        modifier = Modifier
            .size(24.dp)
            .clip(CircleShape)
            .background(if (checked) accent else accent.copy(0.12f)),
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

internal fun IngredientMode.accentColor(): Color = when (this) {
    IngredientMode.Stock -> Colors.PRIMARY
    IngredientMode.Shopping -> Colors.BLUE_TEXT
    IngredientMode.Cooking -> Colors.ORANGE
}

private fun IngredientMode.captionVerb(): String = when (this) {
    IngredientMode.Stock -> "Отметьте, что уже есть дома"
    IngredientMode.Shopping -> "Отмечайте по мере покупки"
    IngredientMode.Cooking -> "Отмечайте по мере добавления"
}
