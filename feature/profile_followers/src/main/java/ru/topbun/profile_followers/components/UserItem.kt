package ru.topbun.profile_followers.components

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
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import ru.topbun.core.ui.R
import ru.topbun.core.ui.components.AppAsyncImage
import ru.topbun.core.ui.theme.Colors
import ru.topbun.core.ui.theme.Typography
import ru.topbun.core.ui.utils.rippleClickable
import ru.topbun.domain.entity.account.ProfileEntity

@Composable
internal fun UserItem(
    profile: ProfileEntity,
    onClick: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(28.dp))
            .background(Colors.WHITE)
            .rippleClickable(Colors.PRIMARY, onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        Avatar(profile.photoUrl)
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(2.dp)
        ) {
            Text(
                text = profile.username.ifBlank { "—" },
                style = Typography.H3,
                color = Colors.MAIN_TEXT,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = followersText(profile.countFollowers),
                style = Typography.S,
                color = Colors.SECONDARY_TEXT,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
        Icon(
            modifier = Modifier
                .size(20.dp)
                .rotate(-90f),
            painter = painterResource(R.drawable.ic_chevron_down),
            contentDescription = null,
            tint = Colors.SECONDARY_TEXT
        )
    }
}

private fun followersText(count: Int): String =
    "${formatCount(count)} ${pluralFollowers(count)}"

private fun formatCount(value: Int): String = when {
    value >= 1_000_000 -> "${value / 1_000_000}.${(value / 100_000) % 10}M"
    value >= 1_000 -> "${value / 1_000}.${(value / 100) % 10}K"
    else -> value.toString()
}

private fun pluralFollowers(count: Int): String {
    val mod10 = count % 10
    val mod100 = count % 100
    return when {
        mod100 in 11..14 -> "подписчиков"
        mod10 == 1 -> "подписчик"
        mod10 in 2..4 -> "подписчика"
        else -> "подписчиков"
    }
}

@Composable
private fun Avatar(url: String?) {
    Box(
        modifier = Modifier
            .size(52.dp)
            .clip(CircleShape)
            .background(Colors.FORM)
            .border(1.dp, Colors.OUTLINE.copy(0.5f), CircleShape),
        contentAlignment = Alignment.Center
    ) {
        if (url.isNullOrBlank()) {
            Icon(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(12.dp),
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
