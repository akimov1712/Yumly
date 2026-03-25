package ru.topbun.auth_login.components

import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import ru.topbun.core.ui.R
import ru.topbun.core.ui.components.AppTextField

@Composable
internal fun FieldEmail(
    value: String,
    onValueChange: (String) -> Unit
) {
    AppTextField(
        text = value,
        onValueChange = onValueChange,
        placeholder = "Email",
        modifier = Modifier
            .fillMaxWidth()
            .defaultMinSize(minHeight = 56.dp),
        startIcon = painterResource(R.drawable.ic_email),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
    )
}
