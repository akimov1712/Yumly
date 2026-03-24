package ru.topbun.auth_login.components

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
import androidx.compose.ui.unit.dp
import ru.topbun.core.ui.R
import ru.topbun.core.ui.components.AppTextField
import ru.topbun.core.ui.theme.Colors
import ru.topbun.core.ui.utils.noRippleClickable

@Composable
internal fun FieldPassword() {
    AppTextField(
        text = "",
        placeholder = "Password",
        modifier = Modifier
            .fillMaxWidth()
            .defaultMinSize(minHeight = 56.dp),
        startIcon = painterResource(R.drawable.ic_password),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
        endIcon = {
            Icon(
                modifier = Modifier
                    .size(24.dp)
                    .clip(CircleShape)
                    .noRippleClickable() {

                    },
                painter = painterResource(R.drawable.ic_password_hide),
                contentDescription = null,
                tint = Colors.BLUE_TEXT,
            )
        }
    ) {

    }
}
