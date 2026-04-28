package ru.topbun.auth_register.components

import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import ru.topbun.core.ui.R
import ru.topbun.core.ui.components.AppOutlinedTextField

@Composable
internal fun FieldUsername(
    value: String,
    error: String?,
    onFocused: () -> Unit,
    onValueChange: (String) -> Unit
) {
    AppOutlinedTextField(
        text = value,
        errorText = error,
        onFocused = onFocused,
        onValueChange = onValueChange,
        placeholder = "Имя пользователя",
        modifier = Modifier
            .fillMaxWidth()
            .defaultMinSize(minHeight = 56.dp),
        startIcon = painterResource(R.drawable.ic_user),
    )
}
