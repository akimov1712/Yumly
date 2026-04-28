package ru.topbun.bmi.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.topbun.bmi.BmiCategory
import ru.topbun.bmi.BmiState
import ru.topbun.core.ui.components.Height
import ru.topbun.core.ui.theme.Colors
import ru.topbun.core.ui.theme.Fonts
import ru.topbun.core.ui.theme.Typography

@Composable
internal fun BmiResultCard(state: BmiState) {
    val category = state.category
    val accent = category.color
    val animatedProgress by animateFloatAsState(
        targetValue = state.categoryProgress,
        animationSpec = tween(420),
        label = "bmi_progress"
    )

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(32.dp))
            .background(
                Brush.linearGradient(
                    colors = listOf(accent, accent.copy(alpha = 0.78f)),
                    start = Offset(0f, 0f),
                    end = Offset(900f, 900f)
                )
            )
            .padding(horizontal = 24.dp, vertical = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Ваш индекс",
            style = Typography.S,
            color = Colors.WHITE.copy(alpha = 0.85f)
        )
        Height(8.dp)
        Row(
            verticalAlignment = Alignment.Bottom,
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Text(
                text = state.bmiRounded.toString(),
                color = Colors.WHITE,
                fontSize = 56.sp,
                fontFamily = Fonts.INTER,
                fontWeight = FontWeight.Bold
            )
            Text(
                modifier = Modifier.padding(bottom = 12.dp),
                text = "кг/м²",
                color = Colors.WHITE.copy(alpha = 0.85f),
                fontSize = 14.sp,
                fontFamily = Fonts.INTER,
                fontWeight = FontWeight.Medium
            )
        }
        Height(6.dp)
        Text(
            text = category.title,
            color = Colors.WHITE,
            fontSize = 18.sp,
            fontFamily = Fonts.INTER,
            fontWeight = FontWeight.Bold
        )
        Height(4.dp)
        Text(
            text = category.description,
            color = Colors.WHITE.copy(alpha = 0.92f),
            fontSize = 13.sp,
            fontFamily = Fonts.INTER,
            fontWeight = FontWeight.Medium,
        )
        Height(20.dp)
        BmiScale(progress = animatedProgress)
    }
}

@Composable
private fun BmiScale(progress: Float) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(34.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(8.dp)
                .clip(RoundedCornerShape(4.dp))
                .align(Alignment.Center)
        ) {
            ScaleSegment(weight = 0.18f, color = BmiCategory.Underweight.color)
            ScaleSegment(weight = 0.25f, color = BmiCategory.Normal.color)
            ScaleSegment(weight = 0.20f, color = BmiCategory.Overweight.color)
            ScaleSegment(weight = 0.37f, color = BmiCategory.Obese.color)
        }
        Box(
            modifier = Modifier
                .fillMaxWidth(progress.coerceAtLeast(0.005f))
                .height(0.dp)
                .align(Alignment.CenterStart)
        ) {
            Box(
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .size(20.dp)
                    .clip(CircleShape)
                    .background(Colors.WHITE)
            ) {
                Box(
                    modifier = Modifier
                        .align(Alignment.Center)
                        .size(10.dp)
                        .clip(CircleShape)
                        .background(Color.Black.copy(alpha = 0.65f))
                )
            }
        }
    }
}

@Composable
private fun RowScope.ScaleSegment(weight: Float, color: Color) {
    Box(
        modifier = Modifier
            .weight(weight)
            .height(8.dp)
            .background(color)
    )
}

internal val BmiCategory.color: Color
    get() = when (this) {
        BmiCategory.Underweight -> Colors.BLUE_TEXT
        BmiCategory.Normal -> Colors.GREEN
        BmiCategory.Overweight -> Colors.ORANGE
        BmiCategory.Obese -> Colors.RED
    }
