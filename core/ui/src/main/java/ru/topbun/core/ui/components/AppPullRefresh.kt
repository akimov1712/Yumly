package ru.topbun.core.ui.components

import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.PullToRefreshDefaults.Indicator
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import ru.topbun.core.ui.theme.Colors

@Composable
fun AppPullRefresh(
    modifier: Modifier = Modifier,
    onRefresh: () -> Unit,
    content: @Composable BoxScope.() -> Unit,
) {
    val refreshState = rememberPullToRefreshState()

    PullToRefreshBox(
        modifier = modifier,
        isRefreshing = false,
        state = refreshState,
        indicator = {
            Indicator(
                modifier = Modifier.align(Alignment.TopCenter),
                isRefreshing = false,
                containerColor = Colors.WHITE,
                color = Colors.PRIMARY,
                state = refreshState,
            )
        },
        onRefresh = onRefresh,
        content = content
    )
}