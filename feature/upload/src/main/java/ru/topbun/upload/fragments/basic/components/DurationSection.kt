package ru.topbun.upload.fragments.basic.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import ru.topbun.core.ui.components.Height
import ru.topbun.core.ui.components.AppTimePicker
import ru.topbun.core.ui.theme.Colors
import ru.topbun.core.ui.theme.Fonts
import ru.topbun.core.ui.theme.Typography
import ru.topbun.core.ui.utils.formatCookingTime
import ru.topbun.upload.components.SectionWrapper

@Composable
fun DurationSection() = SectionWrapper(
    title = "Время приготовления"
){
    var minutes by remember { mutableIntStateOf(0) }
    AppTimePicker(
        startTimeMinutes = 30,
        onTimeChanged = { hour, minute, _ ->
            minutes = (hour * 60) + minute
        }
    )
    Height(20.dp)
    Text(
        text = buildAnnotatedString {
            append("Выбрано: ")
            withStyle(SpanStyle(color = Colors.PRIMARY, fontFamily = Fonts.INTER, fontWeight = FontWeight.Bold)){
                append(formatCookingTime(minutes))
            }
        },
        color = Colors.MAIN_TEXT,
        style = Typography.H3,
    )
}