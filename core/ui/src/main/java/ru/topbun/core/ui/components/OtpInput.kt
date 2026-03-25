package ru.topbun.core.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.topbun.core.ui.theme.Colors
import ru.topbun.core.ui.theme.Fonts

@Composable
fun OtpInput(
    length: Int = 4,
    onValueChange: (String) -> Unit
) {
    val focusRequesters = remember { List(length) { FocusRequester() } }
    val values = remember { mutableStateListOf(*Array(length) { "" }) }

    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current

    var activeIndex by remember { mutableStateOf(0) }

    Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
        values.forEachIndexed { index, value ->

            OtpCell(
                value = value,
                isFocused = activeIndex == index,
                onValueChange = { newValue ->

                    if (values[index] == newValue) return@OtpCell

                    values[index] = newValue
                    val code = values.joinToString("")
                    onValueChange(code)

                    if (newValue.isNotEmpty()) {
                        if (index < length - 1) {
                            activeIndex = index + 1
                            focusRequesters[index + 1].requestFocus()
                        } else {
                            activeIndex = -1
                            focusManager.clearFocus(force = true)
                            keyboardController?.hide()
                        }
                    } else if (index > 0) {
                        activeIndex = index - 1
                        focusRequesters[index - 1].requestFocus()
                    }
                },
                modifier = Modifier
                    .size(72.dp)
                    .focusRequester(focusRequesters[index])
            )
        }
    }

    LaunchedEffect(Unit) {
        focusRequesters.first().requestFocus()
    }
}

@Composable
fun OtpCell(
    value: String,
    isFocused: Boolean,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val borderColor by animateColorAsState(
        targetValue = when {
            isFocused -> Colors.PRIMARY
            value.isNotEmpty() -> Colors.PRIMARY.copy(alpha = 0.5f)
            else -> Colors.OUTLINE
        },
        label = "borderColor"
    )

    val scale by animateFloatAsState(
        targetValue = if (isFocused) 1.05f else 1f,
        label = "scale"
    )

    OutlinedTextField(
        value = value,
        onValueChange = {
            if (it.length <= 1) onValueChange(it)
        },
        modifier = modifier.scale(scale),
        singleLine = true,
        textStyle = TextStyle(
            color = Colors.BLUE_TEXT,
            fontFamily = Fonts.INTER,
            fontWeight = FontWeight.Medium,
            fontSize = 34.sp,
            textAlign = TextAlign.Center
        ),
        shape = RoundedCornerShape(16.dp),
        colors = OutlinedTextFieldDefaults.colors().copy(
            cursorColor = Colors.PRIMARY,
            focusedTextColor = Colors.MAIN_TEXT,
            unfocusedTextColor = Colors.BLUE_TEXT,
            focusedIndicatorColor = borderColor,
            unfocusedIndicatorColor = borderColor,
        ),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
    )
}