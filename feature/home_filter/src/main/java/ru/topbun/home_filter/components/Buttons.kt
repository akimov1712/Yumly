package ru.topbun.home_filter.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ru.topbun.core.ui.components.AppButton
import ru.topbun.core.ui.theme.Colors

@Composable
internal fun Buttons(
    onClickClear: () -> Unit,
    onClickDone: () -> Unit,
) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 24.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ){
        AppButton(
            modifier = Modifier.weight(1f).defaultMinSize(minHeight = 56.dp),
            text = "Очистить",
            containerColor = Colors.FORM,
            contentColor = Colors.MAIN_TEXT,
            onClick = onClickClear
        )
        AppButton(
            modifier = Modifier.weight(1f).defaultMinSize(minHeight = 56.dp),
            text = "Применить",
            onClick = onClickDone
        )
    }
}