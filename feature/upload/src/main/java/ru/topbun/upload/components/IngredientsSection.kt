package ru.topbun.upload.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.dropShadow
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.shadow.Shadow
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import ru.topbun.core.ui.R
import ru.topbun.core.ui.components.AppOutlinedButton
import ru.topbun.core.ui.components.Height
import ru.topbun.core.ui.theme.Colors
import ru.topbun.core.ui.theme.Typography
import ru.topbun.core.ui.utils.rippleClickable
import ru.topbun.domain.entity.recipe.IngredientEntity
import sh.calvin.reorderable.ReorderableColumn
import sh.calvin.reorderable.ReorderableListItemScope

@Composable
internal fun IngredientsSection(
    ingredients: List<IngredientEntity>,
    onClickAddIngredient: () -> Unit,
    onClickRemoveIngredient: (Int) -> Unit,
    onReorderIngredients: (fromIndex: Int, toIndex: Int) -> Unit
) = SectionWrapper(
    title = buildAnnotatedString {
        append("Ингредиенты")
        append(" ")
        withStyle(SpanStyle(color = Colors.SECONDARY_TEXT)) {
            append("(${ingredients.size})")
        }
    },
    padding = PaddingValues(horizontal = 12.dp)
) {
    ReorderableColumn(
        modifier = Modifier.zIndex(100f),
        list = ingredients,
        onSettle = { fromIndex, toIndex ->
            onReorderIngredients(fromIndex, toIndex)
        },
    ) { index, ingredient, isDragging ->
        key(ingredient) {
            ReorderableItem{
                IngredientItem(
                    isDragging = isDragging,
                    ingredient = ingredient,
                    onClickRemove = { onClickRemoveIngredient(index) },
                )
            }
        }
    }
    if (ingredients.isNotEmpty()){
        Height(24.dp)
    }
    AppOutlinedButton(
        text = "Добавить ингредиент",
        modifier = Modifier
            .padding(horizontal = 12.dp)
            .fillMaxWidth()
            .defaultMinSize(minHeight = 56.dp),
        onClick = onClickAddIngredient,
        borderColor = Colors.OUTLINE,
        contentColor = Colors.BLUE_TEXT,
        startIcon = {
            Icon(
                modifier = Modifier.size(16.dp),
                painter = painterResource(R.drawable.ic_plus),
                contentDescription = null,
                tint = Colors.BLUE_TEXT
            )
        }
    )
}

@Composable
private fun ReorderableListItemScope.IngredientItem(
    isDragging: Boolean,
    ingredient: IngredientEntity,
    onClickRemove: () -> Unit,
) {
    Row(
        modifier = Modifier
            .dropShadow(
                shape = RoundedCornerShape(16.dp),
                shadow = Shadow(
                    radius = 8.dp,
                    color = Colors.BLACK.copy(0.2f),
                    alpha = if (isDragging) 1f else 0f
                )
            ).fillMaxWidth()
            .background(Colors.WHITE, RoundedCornerShape(16.dp))
            .padding(horizontal = 12.dp, vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Box(Modifier.draggableHandle()){
            ReorderHandle()
        }
        IngredientValue(ingredient)
        RoundActionButton(
            iconRes = R.drawable.ic_minus,
            containerColor = Colors.PRIMARY,
            onClick = onClickRemove
        )
    }
}

@Composable
private fun RowScope.IngredientValue(ingredient: IngredientEntity) {
    Row(
        modifier = Modifier
            .weight(1f)
            .defaultMinSize(minHeight = 56.dp)
            .clip(RoundedCornerShape(32.dp))
            .background(Colors.BACKGROUND)
            .padding(horizontal = 20.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            modifier = Modifier.weight(1f),
            text = buildAnnotatedString {
                withStyle(SpanStyle(color = Colors.BLUE_TEXT)) {
                    append(ingredient.name)
                }
                append(" ")
                withStyle(SpanStyle(color = Colors.SECONDARY_TEXT)) {
                    append("— ${ingredient.value}")
                }
            },
            style = Typography.H3,
            color = Colors.BLUE_TEXT
        )
    }
}
