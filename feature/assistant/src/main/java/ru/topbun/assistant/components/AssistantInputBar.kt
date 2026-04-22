package ru.topbun.assistant.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.dropShadow
import androidx.compose.ui.graphics.shadow.Shadow
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import ru.topbun.assistant.R
import ru.topbun.core.ui.components.AppTextField
import ru.topbun.core.ui.theme.Colors
import ru.topbun.core.ui.utils.LocalBottomBarPadding
import ru.topbun.core.ui.utils.useBottomBarPadding

@Composable
internal fun AssistantInputBar(
    modifier: Modifier,
    text: String,
    enabled: Boolean,
    isLoading: Boolean,
    onValueChange: (String) -> Unit,
    onSend: () -> Unit
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp)
            .useBottomBarPadding()
            .dropShadow(
                shape = RoundedCornerShape(36.dp),
                Shadow(radius = 4.dp, alpha = 0.1f)
            )
            .clip(RoundedCornerShape(36.dp))
            .background(Colors.WHITE)
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        AppTextField(
            modifier = Modifier
                .weight(1f)
                .heightIn(min = 56.dp, max = 100.dp),
            text = text,
            placeholder = "Сообщение ассистенту",
            singleLine = false,
            shape = RoundedCornerShape(28.dp),
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Default),
            onValueChange = onValueChange
        )
        IconButton(
            modifier = Modifier
                .width(56.dp)
                .height(56.dp)
                .clip(CircleShape)
                .background(if (enabled || isLoading) Colors.PRIMARY else Colors.SECONDARY_TEXT.copy(alpha = 0.35f)),
            enabled = enabled || isLoading,
            onClick = onSend
        ) {
            if (isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier
                        .width(22.dp)
                        .height(22.dp),
                    color = Colors.WHITE,
                    strokeWidth = 2.4.dp
                )
            } else {
                Icon(
                    painter = painterResource(R.drawable.ic_send),
                    contentDescription = "Отправить",
                    tint = Colors.WHITE
                )
            }
        }
    }
}
