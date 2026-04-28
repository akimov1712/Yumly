package ru.topbun.auth_welcome.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import ru.topbun.core.ui.theme.Colors
import ru.topbun.core.ui.theme.Typography

@Composable
fun TitleWithDescription() {
    Column(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 48.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Text(
            text = "Начните готовить",
            color = Colors.MAIN_TEXT,
            style = ru.topbun.core.ui.theme.Typography.H1,
            textAlign = TextAlign.Center
        )
        Text(
            text = "Присоединяйтесь к нашему сообществу, чтобы готовить вкуснее!",
            color = Colors.SECONDARY_TEXT,
            style = Typography.P1,
            textAlign = TextAlign.Center
        )
    }
}