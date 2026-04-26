package ru.topbun.profile_settings.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import ru.topbun.core.ui.R
import ru.topbun.core.ui.components.BottomDialogWrapper
import ru.topbun.core.ui.components.Height
import ru.topbun.core.ui.theme.Colors
import ru.topbun.core.ui.theme.Typography
import ru.topbun.core.ui.utils.rippleClickable

@Composable
internal fun AvatarActionsDialog(
    hasPhoto: Boolean,
    onDismissRequest: () -> Unit,
    onClickPick: () -> Unit,
    onClickRemove: () -> Unit,
) = BottomDialogWrapper(
    onDismissRequest = onDismissRequest,
    containerColor = Colors.BACKGROUND
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Colors.BACKGROUND)
            .padding(start = 16.dp, end = 16.dp, bottom = 24.dp)
    ) {
        Text(
            modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
            text = "Фото вашего профиля",
            style = Typography.H2,
            color = Colors.MAIN_TEXT,
            textAlign = TextAlign.Center
        )
        Height(16.dp)
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(24.dp))
                .background(Colors.WHITE),
            verticalArrangement = Arrangement.spacedBy(2.dp)
        ) {
            ActionItem(
                iconRes = R.drawable.ic_camera,
                title = "Установить фото",
                onClick = onClickPick
            )
            ActionItem(
                iconRes = R.drawable.ic_close,
                title = "Удалить фото",
                tint = Colors.SECONDARY,
                enabled = hasPhoto,
                onClick = onClickRemove
            )
        }
    }
}

@Composable
private fun ActionItem(
    iconRes: Int,
    title: String,
    tint: Color = Colors.BLUE_TEXT,
    enabled: Boolean = true,
    onClick: () -> Unit,
) {
    val finalTint = if (enabled) tint else Colors.SECONDARY_TEXT.copy(alpha = 0.5f)
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .rippleClickable(enabled = enabled, onClick = onClick)
            .padding(horizontal = 20.dp, vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Icon(
            modifier = Modifier.size(24.dp),
            painter = painterResource(iconRes),
            contentDescription = null,
            tint = finalTint
        )
        Text(
            modifier = Modifier.weight(1f),
            text = title,
            style = Typography.P1,
            color = finalTint
        )
    }
}
