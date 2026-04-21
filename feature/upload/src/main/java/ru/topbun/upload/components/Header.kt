package ru.topbun.upload.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
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
import ru.topbun.core.ui.components.AppButton
import ru.topbun.core.ui.components.AppTextButton
import ru.topbun.core.ui.components.Weight
import ru.topbun.core.ui.components.Width
import ru.topbun.core.ui.theme.Colors
import ru.topbun.core.ui.theme.Typography

@Composable
internal fun Header(
    selectedOrder: Int,
    fragmentsSize: Int,
    showBackButton: Boolean,
    publishButtonEnabled: Boolean,
    publishButtonLoading: Boolean,
    onClickClear: () -> Unit,
    onClickBack: () -> Unit,
    onClickPublish: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        if (showBackButton) {
            AppTextButton(
                text = "Назад",
                containerColor = Colors.BLUE_TEXT,
                textColor = Colors.BLUE_TEXT,
                onClick = { onClickBack.invoke() }
            )
        } else {
            AppTextButton(
                text = "Очистить",
                containerColor = Colors.SECONDARY,
                textColor = Colors.SECONDARY
            ) { onClickClear() }
        }
        Width(20.dp)
        Weight(1f)
        if (showBackButton){
            AppButton(
                text = "Опубликовать",
                modifier = Modifier.defaultMinSize(minHeight = 40.dp),
                enabled = publishButtonEnabled,
                isLoading = publishButtonLoading,
                onClick = onClickPublish
            )
        } else {
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
}
