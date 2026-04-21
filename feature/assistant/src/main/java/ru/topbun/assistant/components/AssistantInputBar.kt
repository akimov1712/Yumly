package ru.topbun.assistant.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import ru.topbun.core.ui.components.AppButton
import ru.topbun.core.ui.components.AppTextField
import ru.topbun.core.ui.theme.Colors
import ru.topbun.core.ui.utils.LocalBottomBarPadding

@Composable
internal fun AssistantInputBar(
    text: String,
    enabled: Boolean,
    isLoading: Boolean,
    onValueChange: (String) -> Unit,
    onSend: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp)
            .padding(bottom = LocalBottomBarPadding.current)
            .clip(RoundedCornerShape(32.dp))
            .background(Colors.WHITE)
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        AppTextField(
            modifier = Modifier
                .weight(1f)
                .defaultMinSize(minHeight = 56.dp),
            text = text,
            placeholder = "Сообщение ассистенту",
            singleLine = false,
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Default),
            onValueChange = onValueChange
        )
        AppButton(
            modifier = Modifier
                .height(56.dp)
                .width(118.dp),
            text = "Отправить",
            enabled = enabled,
            isLoading = isLoading,
            onClick = onSend
        )
    }
}
