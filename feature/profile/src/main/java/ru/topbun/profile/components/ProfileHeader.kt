package ru.topbun.profile.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
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
import androidx.compose.ui.draw.dropShadow
import androidx.compose.ui.graphics.shadow.Shadow
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import ru.topbun.core.ui.R
import ru.topbun.core.ui.theme.Colors
import ru.topbun.core.ui.theme.Typography

@Composable
internal fun ProfileHeader(
    modifier: Modifier = Modifier,
    title: String?,
    showBack: Boolean,
    showSettings: Boolean,
    onClickBack: () -> Unit,
    onClickSettings: () -> Unit,
) {
    Row(
        modifier = modifier
            .padding(horizontal = 12.dp).padding(top = 20.dp)
            .fillMaxWidth()
            .clip(RoundedCornerShape(44.dp))
            .background(Colors.WHITE)
            .padding(10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (showBack) {
            CircleIconButton(
                iconRes = R.drawable.ic_back,
                onClick = onClickBack
            )
        } else {
            Spacer(Modifier.size(48.dp))
        }
        Box(
            modifier = Modifier.weight(1f).padding(horizontal = 8.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = title.orEmpty(),
                style = Typography.H2,
                color = Colors.MAIN_TEXT,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                textAlign = TextAlign.Center
            )
        }
        if (showSettings) {
            CircleIconButton(
                iconRes = R.drawable.ic_settings,
                onClick = onClickSettings
            )
        } else {
            Spacer(Modifier.size(48.dp))
        }
    }
}

@Composable
private fun CircleIconButton(
    iconRes: Int,
    onClick: () -> Unit,
) {
    IconButton(
        modifier = Modifier
            .size(48.dp)
            .dropShadow(
                shape = CircleShape,
                shadow = Shadow(radius = 4.dp, alpha = 0.1f)
            )
            .clip(CircleShape)
            .background(Colors.WHITE),
        onClick = onClick
    ) {
        Icon(
            modifier = Modifier.size(24.dp),
            painter = painterResource(iconRes),
            contentDescription = null,
            tint = Colors.BLUE_TEXT
        )
    }
}
