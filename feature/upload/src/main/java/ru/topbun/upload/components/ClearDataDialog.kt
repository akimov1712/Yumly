package ru.topbun.upload.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import ru.topbun.core.ui.components.AppButton
import ru.topbun.core.ui.components.AppOutlinedButton
import ru.topbun.core.ui.components.BottomDialogWrapper
import ru.topbun.core.ui.components.Height
import ru.topbun.core.ui.theme.Colors
import ru.topbun.core.ui.theme.Typography

@Composable
internal fun ClearDataDialog(
    onDismissRequest: () -> Unit,
    onClickConfirm: () -> Unit,
) = BottomDialogWrapper(
    onDismissRequest = onDismissRequest,
    containerColor = Colors.BACKGROUND
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Colors.BACKGROUND)
            .padding(start = 16.dp, end = 16.dp, bottom = 12.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 24.dp)
        ) {
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = "Очистить данные?",
                style = Typography.H2,
                color = Colors.MAIN_TEXT,
                textAlign = TextAlign.Center
            )
            Height(8.dp)
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = "Вы действительно хотите очистить введенные данные о рецепте?",
                style = Typography.P2,
                color = Colors.SECONDARY_TEXT,
                textAlign = TextAlign.Center
            )
            Height(20.dp)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                AppOutlinedButton(
                    text = "Отмена",
                    modifier = Modifier.weight(1f).defaultMinSize(minHeight = 48.dp),
                    onClick = onDismissRequest
                )
                AppButton(
                    text = "Очистить",
                    modifier = Modifier.weight(1f).defaultMinSize(minHeight = 48.dp),
                    containerColor = Colors.SECONDARY,
                    onClick = onClickConfirm
                )
            }
        }
    }
}
