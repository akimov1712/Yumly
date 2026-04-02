package ru.topbun.auth_register.components

import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import ru.topbun.core.ui.R
import ru.topbun.core.ui.components.AppOutlinedTextField
import ru.topbun.core.ui.theme.Colors
import ru.topbun.core.ui.utils.noRippleClickable

@Composable
internal fun FieldPassword(
    value: String,
    placeholder: String,
    error: String?,
    isShowPassword: Boolean,
    onFocused: () -> Unit,
    onClickShowPassword: () -> Unit,
    onChangeValue: (String) -> Unit,
) {
    AppOutlinedTextField(
        text = value,
        onValueChange = onChangeValue,
        onFocused = onFocused,
        error = error,
        placeholder = placeholder,
        modifier = Modifier
            .fillMaxWidth()
            .defaultMinSize(minHeight = 56.dp),
        startIcon = painterResource(R.drawable.ic_password),
        visualTransformation = if (isShowPassword) VisualTransformation.None else PasswordVisualTransformation(),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
        endIcon = {
            Icon(
                modifier = Modifier
                    .size(24.dp)
                    .clip(CircleShape)
                    .noRippleClickable() {
                        onClickShowPassword()
                    },
                painter = painterResource(
                    if (!isShowPassword) R.drawable.ic_password_hide else R.drawable.ic_password_show
                ),
                contentDescription = null,
                tint = Colors.BLUE_TEXT,
            )
        }
    )
}
