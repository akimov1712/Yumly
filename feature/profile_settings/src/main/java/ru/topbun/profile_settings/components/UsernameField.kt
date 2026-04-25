package ru.topbun.profile_settings.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import ru.topbun.core.ui.R
import ru.topbun.core.ui.components.AppOutlinedTextField

@Composable
internal fun UsernameField(
    value: String,
    onValueChange: (String) -> Unit,
) {
    AppOutlinedTextField(
        text = value,
        modifier = Modifier.fillMaxWidth(),
        startIcon = painterResource(R.drawable.ic_user),
        placeholder = "Имя",
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
        onValueChange = onValueChange
    )
}
