package ru.topbun.auth_register.components

import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import ru.topbun.core.ui.R
import ru.topbun.core.ui.components.AppOutlinedTextField

@Composable
internal fun FieldEmail(
    value: String,
    error: String?,
    onFocused: () -> Unit,
    onValueChange: (String) -> Unit
) {
    AppOutlinedTextField(
        text = value,
        errorText = error,
        onValueChange = onValueChange,
        onFocused = onFocused,
        placeholder = "Email",
        modifier = Modifier
            .fillMaxWidth()
            .defaultMinSize(minHeight = 56.dp),
        startIcon = painterResource(R.drawable.ic_email),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
    )
}
