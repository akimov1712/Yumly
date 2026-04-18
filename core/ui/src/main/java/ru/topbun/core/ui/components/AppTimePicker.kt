package ru.topbun.core.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ozcanalasalvar.datepicker.model.Time
import com.ozcanalasalvar.datepicker.ui.theme.colorLightOnBackground
import com.ozcanalasalvar.datepicker.ui.theme.colorLightPrimary
import com.ozcanalasalvar.datepicker.ui.theme.lightPallet
import com.ozcanalasalvar.wheelview.SelectorOptions
import com.ozcanalasalvar.wheelview.WheelView
import ru.topbun.core.ui.theme.Colors
import ru.topbun.core.ui.theme.Typography

@Composable
fun AppTimePicker(
    startTimeMinutes: Int,
    onTimeChanged: (hours: Int, minutes: Int) -> Unit,
) {
    val offset = 2
    val textSize = 16
    val fontSize = maxOf(13, minOf(19, textSize)).sp

    val hours = remember { (0..23).toList() }
    val minutes = remember { (0..59).toList() }

    var selectedTime by remember {
        mutableStateOf(
            Time(
                hour = startTimeMinutes / 60,
                minute = startTimeMinutes % 60
            )
        )
    }

    LaunchedEffect(startTimeMinutes) {
        selectedTime = Time(
            hour = startTimeMinutes / 60,
            minute = startTimeMinutes % 60
        )
    }

    LaunchedEffect(selectedTime) {
        onTimeChanged(selectedTime.hour, selectedTime.minute)
    }

    val itemHeight = (fontSize.value + 10).dp

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Max)
            .background(colorLightPrimary),
        contentAlignment = Alignment.Center
    ) {

        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 100.dp)
        ) {

            key(selectedTime.hour) {
                WheelView(
                    modifier = Modifier.weight(1f),
                    itemSize = DpSize(200.dp, itemHeight),
                    selection = selectedTime.hour,
                    itemCount = hours.size,
                    rowOffset = offset,
                    selectorOption = SelectorOptions().copy(
                        selectEffectEnabled = true,
                        enabled = false
                    ),
                    onFocusItem = {
                        selectedTime = selectedTime.copy(hour = hours[it])
                    }
                ) {
                    Text(
                        text = "%02d ч".format(hours[it]),
                        textAlign = TextAlign.Start,
                        modifier = Modifier.width(200.dp),
                        fontSize = fontSize,
                        style = Typography.P2,
                        color = Colors.MAIN_TEXT
                    )
                }
            }

            key(selectedTime.minute) {
                WheelView(
                    modifier = Modifier.weight(1f),
                    itemSize = DpSize(200.dp, itemHeight),
                    selection = selectedTime.minute,
                    itemCount = minutes.size,
                    rowOffset = offset,
                    selectorOption = SelectorOptions().copy(
                        selectEffectEnabled = true,
                        enabled = false
                    ),
                    onFocusItem = {
                        selectedTime = selectedTime.copy(minute = minutes[it])
                    }
                ) {
                    Text(
                        text = "%02d мин".format(minutes[it]),
                        textAlign = TextAlign.End,
                        modifier = Modifier.width(200.dp),
                        fontSize = fontSize,
                        style = Typography.P2,
                        color = Colors.MAIN_TEXT
                    )
                }
            }
        }

        Box(
            modifier = Modifier
                .matchParentSize()
                .background(
                    Brush.verticalGradient(colors = lightPallet)
                )
        )

        Column(Modifier.fillMaxSize()) {

            Box(
                Modifier
                    .weight(offset.toFloat())
                    .fillMaxWidth()
                    .background(colorLightOnBackground)
            )

            Box(
                Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(Colors.MAIN_TEXT.copy(alpha = 0.07f))
            )

            Box(
                Modifier
                    .weight(offset.toFloat())
                    .fillMaxWidth()
                    .background(colorLightOnBackground)
            )
        }
    }
}