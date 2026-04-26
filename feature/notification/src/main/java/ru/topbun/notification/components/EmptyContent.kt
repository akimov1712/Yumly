package ru.topbun.notification.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import ru.topbun.core.ui.R
import ru.topbun.core.ui.components.Height
import ru.topbun.core.ui.theme.Colors
import ru.topbun.core.ui.theme.Typography

@Composable
internal fun EmptyContent(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 64.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            modifier = Modifier.size(64.dp),
            painter = painterResource(R.drawable.ic_tabs_notification),
            contentDescription = null,
            tint = Colors.SECONDARY_TEXT
        )
        Height(16.dp)
        Text(
            text = "Уведомлений пока нет",
            style = Typography.H2,
            color = Colors.MAIN_TEXT,
            textAlign = TextAlign.Center
        )
        Height(8.dp)
        Text(
            text = "Здесь будут появляться лайки и подписки",
            style = Typography.P2,
            color = Colors.SECONDARY_TEXT,
            textAlign = TextAlign.Center
        )
    }
}
