package ru.topbun.notification.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import ru.topbun.core.ui.R
import ru.topbun.core.ui.components.AppAsyncImage
import ru.topbun.core.ui.components.Width
import ru.topbun.core.ui.theme.Colors
import ru.topbun.core.ui.theme.Typography
import ru.topbun.core.ui.utils.rippleClickable
import ru.topbun.domain.entity.notification.NotificationEntity
import ru.topbun.domain.entity.notification.NotificationType

@Composable
internal fun NotificationItem(
    notification: NotificationEntity,
    onClickInitiator: () -> Unit,
    onClickRecipe: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(28.dp))
            .background(Colors.WHITE)
            .rippleClickable(onClick = onClickInitiator)
            .padding(horizontal = 16.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Avatar(url = notification.initiator.photoUrl)
        Width(12.dp)
        TextBlock(
            modifier = Modifier.weight(1f),
            notification = notification
        )
        if (notification.type == NotificationType.LIKE && notification.recipe != null) {
            Width(12.dp)
            RecipePreview(
                url = notification.recipe?.smallImage,
                onClick = onClickRecipe
            )
        }
    }
}

@Composable
private fun Avatar(url: String?) {
    Box(
        modifier = Modifier
            .size(56.dp)
            .clip(CircleShape)
            .background(Colors.FORM)
            .border(1.5.dp, Colors.OUTLINE.copy(0.5f), CircleShape),
        contentAlignment = Alignment.Center
    ) {
        if (url.isNullOrBlank()) {
            Icon(
                modifier = Modifier.fillMaxSize().padding(14.dp),
                painter = painterResource(R.drawable.ic_user),
                contentDescription = null,
                tint = Colors.SECONDARY_TEXT
            )
        } else {
            AppAsyncImage(
                modifier = Modifier.fillMaxSize(),
                url = url,
                contentScale = ContentScale.Crop
            )
        }
    }
}

@Composable
private fun TextBlock(
    notification: NotificationEntity,
    modifier: Modifier = Modifier,
) {
    val username = notification.initiator.username
    val action = when (notification.type) {
        NotificationType.LIKE -> "лайкнул ваш рецепт"
        NotificationType.FOLLOW -> "подписался на вас"
    }
    val time = formatNotificationTime(notification.createdAt)

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(2.dp)
    ) {
        Text(
            text = username,
            style = Typography.H3,
            color = Colors.MAIN_TEXT,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
        Text(
            text = buildAnnotatedString {
                append(action)
                append("  ·  ")
                withStyle(SpanStyle(color = Colors.SECONDARY_TEXT)) {
                    append(time)
                }
            },
            style = Typography.P2,
            color = Colors.SECONDARY_TEXT,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Composable
private fun RecipePreview(
    url: String?,
    onClick: () -> Unit,
) {
    Box(
        modifier = Modifier
            .size(56.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(Colors.FORM)
            .rippleClickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        if (url.isNullOrBlank()) {
            Icon(
                modifier = Modifier.fillMaxSize().padding(14.dp),
                painter = painterResource(R.drawable.ic_recipe_preview_placeholder),
                contentDescription = null,
                tint = Colors.SECONDARY_TEXT
            )
        } else {
            AppAsyncImage(
                modifier = Modifier.fillMaxSize(),
                url = url,
                contentScale = ContentScale.Crop
            )
        }
    }
}
