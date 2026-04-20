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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.topbun.core.ui.components.AppButton
import ru.topbun.core.ui.components.BottomDialogWrapper
import ru.topbun.core.ui.components.AppOutlinedButton
import ru.topbun.core.ui.components.AppOutlinedTextField
import ru.topbun.core.ui.components.Height
import ru.topbun.core.ui.theme.Colors
import ru.topbun.core.ui.theme.Typography

@Composable
internal fun AddIngredientDialog(
    onDismissRequest: () -> Unit,
    onClickConfirm: (name: String, value: String) -> Unit,
) {
    var name by rememberSaveable { mutableStateOf("") }
    var value by rememberSaveable { mutableStateOf("") }
    val buttonEnabled = name.isNotBlank() && value.isNotBlank()

    BottomDialogWrapper(
        onDismissRequest = onDismissRequest,
        containerColor = Colors.WHITE
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp, vertical = 16.dp)
        ) {
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = "Добавить ингредиент",
                style = Typography.H2,
                color = Colors.MAIN_TEXT,
                textAlign = TextAlign.Center
            )
            Height(10.dp)
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = "Укажите название и количество ингредиента",
                style = Typography.P2,
                lineHeight = 17.sp,
                color = Colors.SECONDARY_TEXT,
                textAlign = TextAlign.Center
            )
            Height(20.dp)
            AppOutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .defaultMinSize(minHeight = 56.dp),
                text = name,
                placeholder = "Например, Яйцо",
                supportText = "${name.length}/32",
                onValueChange = {
                    if (it.length <= 32) name = it
                }
            )
            Height(12.dp)
            AppOutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .defaultMinSize(minHeight = 56.dp),
                text = value,
                placeholder = "Например, 2 шт",
                supportText = "${value.length}/24",
                onValueChange = {
                    if (it.length <= 24) value = it
                }
            )
            Height(20.dp)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                AppOutlinedButton(
                    text = "Отмена",
                    modifier = Modifier
                        .weight(1f)
                        .defaultMinSize(minHeight = 48.dp),
                    onClick = onDismissRequest,
                    borderColor = Colors.OUTLINE,
                    contentColor = Colors.BLUE_TEXT,
                )
                AppButton(
                    text = "Добавить",
                    modifier = Modifier
                        .weight(1f)
                        .defaultMinSize(minHeight = 48.dp),
                    enabled = buttonEnabled,
                    onClick = { onClickConfirm(name, value) }
                )
            }
        }
    }
}
