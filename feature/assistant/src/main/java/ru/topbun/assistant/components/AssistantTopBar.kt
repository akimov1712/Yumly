package ru.topbun.assistant.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
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

@Composable
internal fun AssistantTopBar(
    onClickChats: () -> Unit
) {
    Row(
        modifier = Modifier
            .padding(horizontal = 12.dp)
            .padding(top = 20.dp)
            .fillMaxWidth()
            .clip(RoundedCornerShape(44.dp))
            .background(Colors.WHITE)
            .padding(horizontal = 10.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        AppTextButton(
            text = "Чаты",
            modifier = Modifier.defaultMinSize(minHeight = 48.dp),
            textColor = Colors.BLUE_TEXT,
            containerColor = Colors.BLUE_TEXT,
            onClick = onClickChats
        )
        Spacer(Modifier.weight(1f))
        Text(
            text = "Assistant",
            color = Colors.MAIN_TEXT,
            style = Typography.H2,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
        Width(20.dp)
        Icon(
            modifier = Modifier.size(28.dp),
            painter = painterResource(R.drawable.ic_tabs_assistant),
            contentDescription = null,
            tint = Colors.PRIMARY
        )
    }
}
