package ru.topbun.home_filter.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import ru.topbun.core.ui.theme.Colors
import ru.topbun.core.ui.theme.Typography

@Composable
internal fun Title() {
    Text(
        modifier = Modifier.fillMaxWidth(),
        text = "Добавить фильтр",
        color = Colors.MAIN_TEXT,
        style = Typography.H2,
        textAlign = TextAlign.Center
    )
}
