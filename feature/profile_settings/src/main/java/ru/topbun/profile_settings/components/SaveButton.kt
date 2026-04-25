package ru.topbun.profile_settings.components

import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ru.topbun.core.ui.components.AppButton

@Composable
internal fun SaveButton(
    enabled: Boolean,
    isLoading: Boolean,
    onClick: () -> Unit,
) {
    AppButton(
        text = "Сохранить",
        modifier = Modifier
            .fillMaxWidth()
            .defaultMinSize(minHeight = 56.dp),
        enabled = enabled,
        isLoading = isLoading,
        onClick = onClick
    )
}
