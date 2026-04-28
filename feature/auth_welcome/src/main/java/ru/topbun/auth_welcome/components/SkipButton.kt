package ru.topbun.auth_welcome.components

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ru.topbun.core.ui.components.AppTextButton

@Composable
internal fun ColumnScope.SkipButton(onClick: () -> Unit) {
    AppTextButton(
        text = "Позже",
        modifier = Modifier
            .align(Alignment.End)
            .padding(horizontal = 16.dp),
        onClick = onClick
    )
}