package ru.topbun.upload.components

import android.R.attr.text
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
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
    nutrients: Map<NutrientsEnum, Int>,
    totalCalories: Int,
    sumLimitExceeded: Boolean,
    onChangeValue: (value: Int, nutrient: NutrientsEnum) -> Unit
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
    NutrientsList(nutrients, sumLimitExceeded, onChangeValue)
    Height(20.dp)
    Text(
        modifier = Modifier.fillMaxWidth(),
        textAlign = TextAlign.End,
        text = "Всего $totalCalories ккал",
        color = Colors.SECONDARY_TEXT,
        style = Typography.P2
    )
}

@Composable
private fun NutrientsList(
    nutrients: Map<NutrientsEnum, Int>,
    sumLimitExceeded: Boolean,
    onChangeValue: (value: Int, nutrient: NutrientsEnum) -> Unit
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        nutrients.map {
            NutrientItem(
                nutrient = it.key,
                value = it.value,
                sumLimitExceeded = sumLimitExceeded,
                onChangeValue = onChangeValue
            )
        }
    }
}

@Composable
private fun NutrientItem(
    nutrient: NutrientsEnum,
    value: Int,
    sumLimitExceeded: Boolean,
    onChangeValue: (value: Int, nutrient: NutrientsEnum) -> Unit
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
        BottomNutrientItem(
            value = value,
            sumLimitExceeded = sumLimitExceeded,
            buttonBackground = buttonBackground,
        ){
            onChangeValue(it, nutrient)
        }
    }
}

@Composable
private fun BottomNutrientItem(
    value: Int,
    sumLimitExceeded: Boolean,
    buttonBackground: Brush,
    onValueChange: (Int) -> Unit,
) {
    var oldValue by remember { mutableStateOf(0) }
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        NutrientButton(
            icon = painterResource(R.drawable.ic_minus),
            background = buttonBackground,
            isEnabled = value > 0,
            onClick = {
                val newValue = value - 1
                onValueChange(newValue)
            }
        )
        Box(
            modifier = Modifier
                .weight(1f)
                .defaultMinSize(minHeight = 40.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(Colors.BACKGROUND),
            contentAlignment = Alignment.Center
        ){
            BasicTextField(
                modifier = Modifier
                    .fillMaxWidth(),
                textStyle = Typography.H2.copy(textAlign = TextAlign.Center),
                value = value.toString(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                cursorBrush = SolidColor(Colors.PRIMARY),
                onValueChange = { newValue ->
                    val newValue = if (oldValue == 0) newValue.replace("0","") else newValue
                    val newValueInt = newValue.toIntOrNull() ?: 0
                    if (newValueInt in 0..100){
                        onValueChange(newValueInt)
                    }
                    oldValue = newValueInt
                }
            )
        }
        NutrientButton(
            icon = painterResource(R.drawable.ic_plus),
            background = buttonBackground,
            isEnabled = value < 100 && !sumLimitExceeded,
            onClick = {
                val newValue = value + 1
                onValueChange(newValue)
            }
        )
    }
}

@Composable
private fun NutrientButton(
    icon: Painter,
    background: Brush,
    isEnabled: Boolean,
    onClick: () -> Unit
) {

    Box(
        modifier = Modifier
            .size(40.dp)
            .clip(RoundedCornerShape(12.dp))
            .alpha(if(!isEnabled) 0.6f else 1f)
            .background(background)
            .rippleClickable(enabled = isEnabled, color = Colors.WHITE){ onClick() }
            .padding(14.dp),
        contentAlignment = Alignment.Center
    ) {
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