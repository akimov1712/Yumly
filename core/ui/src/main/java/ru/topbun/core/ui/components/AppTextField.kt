package ru.topbun.core.ui.components

import android.R.attr.singleLine
import android.R.attr.text
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.InputTransformation.Companion.keyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import ru.topbun.core.ui.theme.Colors
import ru.topbun.core.ui.theme.Typography

@Composable
fun AppTextField(
    text: String,
    placeholder: String,
    modifier: Modifier = Modifier,
    startIcon: Painter? = null,
    endIcon: (@Composable () -> Unit)? = null,
    singleLine: Boolean = true,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    onValueChange: (String) -> Unit
) {
    OutlinedTextField(
        modifier = modifier,
        value = text,
        onValueChange = onValueChange,
        singleLine = singleLine,
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
        shape = RoundedCornerShape(32.dp),
        colors = OutlinedTextFieldDefaults.colors().copy(
            cursorColor = Colors.PRIMARY,
            focusedTextColor = Colors.MAIN_TEXT,
            unfocusedTextColor = Colors.BLUE_TEXT,
            disabledTextColor = Colors.BLUE_TEXT,
            errorTextColor = Colors.BLUE_TEXT,
            focusedIndicatorColor = Colors.PRIMARY,
            unfocusedIndicatorColor = Colors.OUTLINE,
            errorIndicatorColor = Colors.SECONDARY,
        ),
        keyboardOptions = keyboardOptions
    )
}