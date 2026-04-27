package ru.topbun.profile_followers.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import ru.topbun.core.ui.theme.Colors
import ru.topbun.core.ui.theme.Typography
import ru.topbun.core.ui.utils.noRippleClickable
import ru.topbun.navigation.ProfileScreenProvider

@Composable
internal fun FollowTabsBar(
    selectedTab: ProfileScreenProvider.FollowsTab,
    followersCount: Int,
    followingCount: Int,
    modifier: Modifier = Modifier,
    onSelect: (ProfileScreenProvider.FollowsTab) -> Unit,
) {
    Row(
        modifier = modifier
            .padding(horizontal = 12.dp)
            .fillMaxWidth()
            .clip(RoundedCornerShape(44.dp))
            .background(Colors.WHITE)
            .padding(10.dp),
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        TabItem(
            title = "Подписчики",
            count = followersCount,
            selected = selectedTab == ProfileScreenProvider.FollowsTab.Followers,
            onClick = { onSelect(ProfileScreenProvider.FollowsTab.Followers) }
        )
        TabItem(
            title = "Подписки",
            count = followingCount,
            selected = selectedTab == ProfileScreenProvider.FollowsTab.Following,
            onClick = { onSelect(ProfileScreenProvider.FollowsTab.Following) }
        )
    }
}

@Composable
private fun RowScope.TabItem(
    title: String,
    count: Int,
    selected: Boolean,
    onClick: () -> Unit,
) {
    val color by animateColorAsState(
        targetValue = if (selected) Colors.PRIMARY else Colors.SECONDARY_TEXT,
        animationSpec = tween(durationMillis = 220)
    )
    val indicatorWidth by animateDpAsState(
        targetValue = if (selected) 42.dp else 0.dp,
        animationSpec = tween(durationMillis = 220)
    )

    val label = buildAnnotatedString {
        append(title)
        append(' ')
        withStyle(SpanStyle(color = Colors.SECONDARY_TEXT)) {
            append("(${formatCount(count)})")
        }
    }

    Column(
        modifier = Modifier
            .weight(1f)
            .clip(RoundedCornerShape(33.dp))
            .noRippleClickable(onClick)
            .padding(top = 10.dp, bottom = 6.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(5.dp)
    ) {
        Text(
            text = label,
            style = Typography.H3,
            color = color
        )
        Box(
            modifier = Modifier
                .size(width = indicatorWidth, height = 3.dp)
                .background(Colors.PRIMARY, RoundedCornerShape(2.dp))
        )
    }
}

private fun formatCount(value: Int): String = when {
    value >= 1_000_000 -> "${value / 1_000_000}.${(value / 100_000) % 10}M"
    value >= 1_000 -> "${value / 1_000}.${(value / 100) % 10}K"
    else -> value.toString()
}
