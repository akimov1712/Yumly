package ru.topbun.auth_welcome.components

import android.widget.Button
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ru.topbun.core.ui.components.AppButton

@Composable
fun Button(onClick: () -> Unit) {
    AppButton(
        text = "Get Started",
        modifier = Modifier.fillMaxWidth()
            .defaultMinSize(minHeight = 56.dp)
            .padding(horizontal = 24.dp),
        onClick = onClick
    )
}