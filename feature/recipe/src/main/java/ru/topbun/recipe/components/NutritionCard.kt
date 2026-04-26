package ru.topbun.recipe.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.topbun.core.ui.components.Height
import ru.topbun.core.ui.theme.Colors
import ru.topbun.core.ui.theme.Fonts
import ru.topbun.core.ui.theme.Typography
import ru.topbun.domain.entity.recipe.RecipeEntity

@Composable
internal fun NutritionCard(
    recipe: RecipeEntity,
    modifier: Modifier = Modifier,
) {
    val total = (recipe.protein + recipe.fat + recipe.carb).coerceAtLeast(1.0)
    val proteinShare = (recipe.protein / total).toFloat()
    val fatShare = (recipe.fat / total).toFloat()
    val carbShare = (recipe.carb / total).toFloat()

    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(28.dp))
            .background(Colors.WHITE)
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Пищевая ценность",
                style = Typography.H2,
                color = Colors.MAIN_TEXT
            )
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            CaloriesBlock(kcal = recipe.kcal)
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                NutrientBar(
                    title = "Белки",
                    grams = recipe.protein,
                    fraction = proteinShare,
                    color = Colors.GREEN
                )
                NutrientBar(
                    title = "Жиры",
                    grams = recipe.fat,
                    fraction = fatShare,
                    color = Colors.ORANGE
                )
                NutrientBar(
                    title = "Углеводы",
                    grams = recipe.carb,
                    fraction = carbShare,
                    color = Colors.RED
                )
            }
        }
    }
}

@Composable
private fun CaloriesBlock(kcal: Int) {
    Column(
        modifier = Modifier
            .size(110.dp)
            .clip(RoundedCornerShape(24.dp))
            .background(Colors.SECONDARY.copy(0.08f)),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = kcal.toString(),
            color = Colors.SECONDARY,
            fontFamily = Fonts.INTER,
            fontWeight = FontWeight.Bold,
            fontSize = 30.sp,
            textAlign = TextAlign.Center
        )
        Text(
            text = "ккал",
            style = Typography.S,
            color = Colors.SECONDARY,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
private fun NutrientBar(
    title: String,
    grams: Double,
    fraction: Float,
    color: Color,
) {
    var animateTrigger by remember { mutableStateOf(false) }
    LaunchedEffect(fraction) { animateTrigger = true }
    val animatedFraction by animateFloatAsState(
        targetValue = if (animateTrigger) fraction.coerceIn(0f, 1f) else 0f,
        animationSpec = tween(durationMillis = 700),
        label = "nutrient_bar"
    )

    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                modifier = Modifier.weight(1f),
                text = title,
                style = Typography.P2,
                color = Colors.MAIN_TEXT
            )
            Text(
                text = buildAnnotatedString {
                    append("${formatGrams(grams)} ")
                    withStyle(SpanStyle(color = Colors.SECONDARY_TEXT)) { append("г") }
                },
                style = Typography.H3,
                color = color
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
                    .fillMaxWidth(animatedFraction)
                    .height(8.dp)
                    .clip(RoundedCornerShape(4.dp))
                    .background(color)
            )
        }
    }
}

private fun formatGrams(value: Double): String =
    if (value % 1.0 == 0.0) value.toInt().toString() else String.format("%.1f", value)
