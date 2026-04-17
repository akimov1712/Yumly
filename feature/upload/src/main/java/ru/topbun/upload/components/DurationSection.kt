package ru.topbun.upload.components

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import ru.topbun.core.ui.components.AppTimePicker
import ru.topbun.core.ui.components.Height
import ru.topbun.core.ui.theme.Colors
import ru.topbun.core.ui.theme.Fonts
import ru.topbun.core.ui.theme.Typography
import ru.topbun.core.ui.utils.formatCookingTime

@Composable
internal fun DurationSection(cookingTime: Int, onChangeDuration: (Int) -> Unit) = SectionWrapper(
    title = "Время приготовления"
){
    AppTimePicker(cookingTime){ hour, minute ->
        onChangeDuration((hour * 60) + minute)
    }
    Height(20.dp)
    Text(
        text = buildAnnotatedString {
            append("Выбрано: ")
            withStyle(SpanStyle(color = Colors.PRIMARY, fontFamily = Fonts.INTER, fontWeight = FontWeight.Bold)){
                append(formatCookingTime(cookingTime))
            }
        },
        color = Colors.MAIN_TEXT,
        style = Typography.H3,
    )
}