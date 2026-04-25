package ru.topbun.profile.components

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
import androidx.compose.ui.unit.dp
import ru.topbun.core.ui.theme.Colors
import ru.topbun.core.ui.theme.Typography
import ru.topbun.core.ui.utils.noRippleClickable
import ru.topbun.profile.ProfileState

@Composable
internal fun ProfileTabsBar(
    selectedTab: ProfileState.ProfileTab,
    tabs: List<ProfileState.ProfileTab> = ProfileState.ProfileTab.entries,
    onSelect: (ProfileState.ProfileTab) -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(44.dp))
            .background(Colors.WHITE)
            .padding(10.dp),
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        tabs.forEach { tab ->
            TabItem(
                title = tab.title,
                selected = tab == selectedTab,
                onClick = { onSelect(tab) }
            )
        }
    }
}

@Composable
private fun RowScope.TabItem(
    title: String,
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
            text = title,
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
