package ru.topbun.core.ui.components

import android.R.attr.textStyle
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.topbun.core.ui.theme.Colors
import ru.topbun.core.ui.theme.Typography

@Composable
fun AppOutlinedTextField(
    text: String,
    placeholder: String,
    modifier: Modifier = Modifier,
    startIcon: Painter? = null,
    errorText: String? = null,
    supportText: String? = null,
    endIcon: (@Composable () -> Unit)? = null,
    singleLine: Boolean = true,
    shape: RoundedCornerShape = RoundedCornerShape(32.dp),
    visualTransformation: VisualTransformation = VisualTransformation.None,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    onFocused: () -> Unit = {},
    onValueChange: (String) -> Unit
) {
    OutlinedTextField(
        modifier = modifier.onFocusChanged{ if (it.isFocused){ onFocused() } },
        value = text,
        onValueChange = onValueChange,
        visualTransformation = visualTransformation,
        isError = errorText != null,
        singleLine = singleLine,
        supportingText = if (errorText != null || supportText != null){
            {
                Text(
                    modifier = Modifier.fillMaxWidth().padding(4.dp),
                    text = errorText ?: supportText ?: "",
                    color = if (errorText != null) Colors.ERROR else Colors.BLUE_TEXT.copy(0.5f),
                    style = Typography.P2.copy(fontSize = 14.sp),
                )
            }
        } else null,
        placeholder = {
            Text(
                text = placeholder,
                style = Typography.Placeholder,
                color = Colors.BLUE_TEXT.copy(0.5f)
            )
        },
        prefix = {
            Row {
                Width(8.dp)
                startIcon?.let {
                    Icon(
                        painter = it,
                        contentDescription = null,
                        tint = Colors.BLUE_TEXT
                    )
                    Width(8.dp)
                }
            }
        },
        suffix = {
            Row{
                endIcon?.let { endIconContent ->
                    Width(8.dp)
                    endIconContent()
                }
                Width(8.dp)
            }
        },
        textStyle = Typography.Placeholder,
        shape = shape,
        colors = OutlinedTextFieldDefaults.colors().copy(
            cursorColor = Colors.PRIMARY,
            focusedTextColor = Colors.MAIN_TEXT,
            unfocusedTextColor = Colors.BLUE_TEXT,
            disabledTextColor = Colors.BLUE_TEXT,
            errorTextColor = Colors.BLUE_TEXT,
            focusedIndicatorColor = Colors.PRIMARY,
            unfocusedIndicatorColor = Colors.OUTLINE,
            errorIndicatorColor = Colors.ERROR,
        ),
        keyboardOptions = keyboardOptions,
    )
}