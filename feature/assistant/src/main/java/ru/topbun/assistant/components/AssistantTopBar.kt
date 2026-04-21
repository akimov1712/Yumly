package ru.topbun.assistant.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import ru.topbun.core.ui.R
import ru.topbun.core.ui.components.AppTextButton
import ru.topbun.core.ui.components.Width
import ru.topbun.core.ui.theme.Colors
import ru.topbun.core.ui.theme.Typography
import ru.topbun.core.ui.utils.rippleClickable

@Composable
internal fun AssistantTopBar(
    onClickHistory: () -> Unit,
    onClickNewChat: () -> Unit
) {
    Row(
        modifier = Modifier
            .padding(horizontal = 12.dp)
            .padding(top = 20.dp)
            .fillMaxWidth()
            .clip(RoundedCornerShape(32.dp))
            .background(Colors.WHITE)
            .padding(horizontal = 10.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(
            modifier = Modifier
                .size(48.dp)
                .clip(CircleShape)
                .background(Colors.FORM),
            onClick = onClickHistory
        ) {
            Icon(
                modifier = Modifier.size(24.dp),
                painter = painterResource(R.drawable.ic_tabs_assistant),
                contentDescription = null,
                tint = Colors.BLUE_TEXT
            )
        }
        Width(12.dp)
        Text(
            text = "Yumly Assistant",
            color = Colors.MAIN_TEXT,
            style = Typography.H2,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
        Spacer(Modifier.weight(1f))
        AppTextButton(
            text = "Новый",
            textColor = Colors.PRIMARY,
            containerColor = Colors.PRIMARY,
            onClick = onClickNewChat
        )
        Width(2.dp)
        Text(
            modifier = Modifier
                .clip(RoundedCornerShape(20.dp))
                .background(Colors.FORM)
                .padding(horizontal = 12.dp, vertical = 8.dp)
                .rippleClickable(onClick = onClickHistory),
            text = "История",
            color = Colors.SECONDARY_TEXT,
            style = Typography.S
        )
    }
}
