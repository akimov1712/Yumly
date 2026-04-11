package ru.topbun.upload.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import ru.topbun.core.ui.R
import ru.topbun.core.ui.components.AppTextField
import ru.topbun.core.ui.components.Height
import ru.topbun.core.ui.theme.Colors
import ru.topbun.core.ui.theme.Fonts
import ru.topbun.core.ui.theme.Typography
import ru.topbun.core.ui.utils.rippleClickable
import ru.topbun.upload.UploadState.NutrientsEnum

@Composable
internal fun NutrientsSection(
    nutrients: Map<NutrientsEnum, Int>
) = SectionWrapper(
    title = buildAnnotatedString {
        append("Нутриенты")
        withStyle(
            SpanStyle(
                color = Colors.SECONDARY_TEXT,
                fontFamily = Fonts.INTER,
                fontWeight = FontWeight.Medium
            )
        ) {
            append(" (на 100 г)")
        }
    }
) {
    NutrientsList(nutrients)
}

@Composable
private fun NutrientsList(nutrients: Map<NutrientsEnum, Int>) {
    Column(
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        nutrients.map { NutrientItem(it.key, it.value) }
    }
}

@Composable
private fun NutrientItem(
    nutrient: NutrientsEnum,
    value: Int
) {
    val calories = value * nutrient.calories

    val buttonBackground = when(nutrient){
        NutrientsEnum.Protein -> listOf(Color(0xFFD2A4A4), Color(0xffEF2626))
        NutrientsEnum.Fat -> listOf(Color(0xffFFA238), Color(0xffDB7114))
        NutrientsEnum.Carbs -> listOf(Color(0xff54F4A7), Color(0xff1FCC79))
    }.let { Brush.linearGradient(it) }
    Column{
        HeaderNutrientItem(nutrient, calories)
        Height(10.dp)
        BottomNutrientItem(buttonBackground)
    }
}

@Composable
private fun BottomNutrientItem(buttonBackground: Brush) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        NutrientButton(
            icon = painterResource(R.drawable.ic_minus),
            background = buttonBackground,
            onClick = { }
        )
        AppTextField(
            modifier = Modifier
                .weight(1f)
                .defaultMinSize(minHeight = 40.dp),
            text = "0",
            placeholder = "",
            textStyle = Typography.H2.copy(textAlign = TextAlign.Center),
            shape = RoundedCornerShape(12.dp)
        ) { }
        NutrientButton(
            icon = painterResource(R.drawable.ic_plus),
            background = buttonBackground,
            onClick = { }
        )
    }
}

@Composable
private fun NutrientButton(
    icon: Painter,
    background: Brush,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier.size(40.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(background)
            .rippleClickable(color = Colors.WHITE, onClick = onClick)
            .padding(14.dp),
        contentAlignment = Alignment.Center
    ){
        Icon(
            painter = icon,
            contentDescription = null,
            tint = Colors.WHITE
        )
    }
}

@Composable
private fun HeaderNutrientItem(nutrient: NutrientsEnum, calories: Int) {
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
        Text(
            text = nutrient.title + ", г",
            color = Colors.MAIN_TEXT,
            style = Typography.P2
        )
        Text(
            text = "$calories ккал",
            color = Colors.SECONDARY_TEXT,
            style = Typography.P2
        )
    }
}