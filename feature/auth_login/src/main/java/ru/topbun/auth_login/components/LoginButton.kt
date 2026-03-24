package ru.topbun.auth_login.components

import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ru.topbun.core.ui.components.AppButton

@Composable
internal fun LoginButton() {
    AppButton(
        modifier = Modifier
            .fillMaxWidth()
            .defaultMinSize(minHeight = 56.dp),
        text = "Login"
    ) { }
}

