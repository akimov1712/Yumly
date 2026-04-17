package ru.topbun.upload.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import ru.topbun.core.ui.components.AppTextButton
import ru.topbun.core.ui.components.Weight
import ru.topbun.core.ui.theme.Colors
import ru.topbun.core.ui.theme.Typography

@Composable
internal fun Header(
    selectedOrder: Int,
    fragmentsSize: Int,
    onClickClear: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        AppTextButton(
            text = "Очистить",
            containerColor = Colors.SECONDARY,
            textColor = Colors.SECONDARY
        ) { onClickClear() }
        Weight(1f)
        Text(
            text = buildAnnotatedString {
                append("$selectedOrder/")
                withStyle(SpanStyle(color = Colors.SECONDARY_TEXT)) { append(fragmentsSize.toString()) }
            },
            style = Typography.H2,
            color = Colors.MAIN_TEXT
        )
    }
}