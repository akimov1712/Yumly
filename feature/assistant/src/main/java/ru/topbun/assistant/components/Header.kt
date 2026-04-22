package ru.topbun.assistant.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.dropShadow
import androidx.compose.ui.graphics.shadow.Shadow
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.topbun.core.ui.R
import ru.topbun.core.ui.components.AppButton
import ru.topbun.core.ui.components.AppTextButton
import ru.topbun.core.ui.theme.Colors

@Composable
internal fun Header(
    modifier: Modifier,
    onClickHistory: () -> Unit,
    onClickNewChat: () -> Unit
) {
    Row(
        modifier = modifier
            .padding(horizontal = 12.dp)
            .padding(top = 20.dp)
            .fillMaxWidth()
            .padding(horizontal = 10.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(
            modifier = Modifier
                .size(48.dp)
                .dropShadow(
                    shape = CircleShape,
                    Shadow(
                        radius = 4.dp,
                        alpha = 0.1f,
                    )
                ).clip(CircleShape)
                .background(Colors.WHITE),
            onClick = onClickHistory
        ) {
            Icon(
                modifier = Modifier.size(24.dp),
                painter = painterResource(R.drawable.ic_tabs_assistant),
                contentDescription = null,
                tint = Colors.BLUE_TEXT
            )
        }
        Spacer(Modifier.weight(1f))
        AppButton(
            modifier = Modifier.defaultMinSize(minHeight = 48.dp)
                .dropShadow(
                    shape = RoundedCornerShape(32.dp),
                    Shadow(radius = 4.dp, alpha = 0.1f)
                ),
            text = "Новый",
            onClick = onClickNewChat,
        )

    }
}
