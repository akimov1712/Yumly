package ru.topbun.core.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ozcanalasalvar.datepicker.compose.component.SelectorView
import com.ozcanalasalvar.datepicker.model.Time
import com.ozcanalasalvar.datepicker.ui.theme.colorLightPrimary
import com.ozcanalasalvar.datepicker.ui.theme.lightPallet
import com.ozcanalasalvar.wheelview.SelectorOptions
import com.ozcanalasalvar.wheelview.WheelView
import ru.topbun.core.ui.theme.Colors
import ru.topbun.core.ui.theme.Typography


@Composable
fun AppTimePicker(
    offset: Int = 2,
    startTimeMinutes: Int = 0,
    textSize: Int = 16,
    onTimeChanged: (Int, Int, String?) -> Unit = { _, _, _ -> },
) {
    val startTime = Time(hour = startTimeMinutes / 60, minute = startTimeMinutes % 60)
    var selectedTime by remember { mutableStateOf(startTime) }

    val hours = mutableListOf<Int>().apply {
        for (hour in 0..23) {
            add(hour)
        }
    }

    val minutes = mutableListOf<Int>().apply {
        for (minute in 0..59) {
            add(minute)
        }
    }
    val fontSize = maxOf(13, minOf(19, textSize))

    LaunchedEffect(selectedTime) {
        onTimeChanged(selectedTime.hour, selectedTime.minute, selectedTime.format)
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Max)
            .background(colorLightPrimary),
        contentAlignment = Alignment.Center
    ) {

        val height=( fontSize + 10) .dp


        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(start = 100.dp, end = 100.dp)
        ) {


            WheelView(modifier = Modifier.weight(3f),
                itemSize = DpSize(200.dp, height),
                selection = 0,
                itemCount = hours.size,
                rowOffset = offset,
                selectorOption = SelectorOptions().copy(selectEffectEnabled = true, enabled = false),
                onFocusItem = {
                    selectedTime = selectedTime.copy(hour = hours[it])
                },
                content = {
                    Text(
                        text = (if (hours[it] < 10) "0${hours[it]}" else "${hours[it]}") + " ч",
                        textAlign = TextAlign.Start,
                        modifier = Modifier.width(200.dp),
                        fontSize = fontSize.sp,
                        style = Typography.P2,
                        color = Colors.MAIN_TEXT
                    )
                })


            WheelView(modifier = Modifier.weight(3f),
                itemSize = DpSize(200.dp, height),
                selection = 0,
                itemCount = minutes.size,
                rowOffset = offset,
                selectorOption = SelectorOptions().copy(selectEffectEnabled = true, enabled = false),
                onFocusItem = {
                    selectedTime = selectedTime.copy(minute = minutes[it])
                },
                content = {
                    Text(
                        text = (if (minutes[it] < 10) "0${minutes[it]}" else "${minutes[it]}") + " мин",
                        textAlign = TextAlign.End,
                        modifier = Modifier.width(200.dp),
                        fontSize = fontSize.sp,
                        style = Typography.P2,
                        color = Colors.MAIN_TEXT
                    )
                })
        }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.verticalGradient(
                        colors =  lightPallet
                    )
                ),
        ) {}

        SelectorView(darkModeEnabled= false, offset = offset)

    }
}