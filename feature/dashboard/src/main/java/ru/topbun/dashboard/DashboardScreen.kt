package ru.topbun.dashboard

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.registry.ScreenRegistry
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.navigator.tab.LocalTabNavigator
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabNavigator
import ru.topbun.core.ui.theme.Colors
import ru.topbun.core.ui.theme.Typography
import ru.topbun.core.ui.utils.StatusBarColor
import ru.topbun.core.ui.utils.changeStatusBarColor
import ru.topbun.core.ui.utils.noRippleClickable
import ru.topbun.navigation.DashboardScreenProvider
import kotlin.collections.indexOf

object DashboardScreen: Screen{

    @Composable
    override fun Content() {
        changeStatusBarColor(StatusBarColor.DARK)

        val tabs = listOf(
            DashboardScreenProvider.Home,
            DashboardScreenProvider.Upload,
            DashboardScreenProvider.Assistant,
            DashboardScreenProvider.Notification,
            DashboardScreenProvider.Profile,
        ).map { ScreenRegistry.get(it) as Tab}

        TabNavigator(tabs.first()){
            Box(modifier = Modifier
                .fillMaxSize()
                .background(Colors.BACKGROUND)
            ) {
                CurrentTabTransition(it, tabs)
                BottomBar(tabs)
            }
        }
    }

    @Composable
    private fun BoxScope.BottomBar(tabs: List<Tab>) {
        val tabNavigator = LocalTabNavigator.current
        Row(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .systemBarsPadding()
                .padding(horizontal = 12.dp, vertical = 16.dp)
                .fillMaxWidth()
                .clip(RoundedCornerShape(44.dp))
                .background(Colors.WHITE)
                .padding(horizontal = 16.dp)
        ) {
            tabs.forEach { tab ->
                val title = tab.options.title
                val icon = tab.options.icon ?: throw RuntimeException("Tab icon not exists")
                val selected = tabNavigator.current == tab
                BottomBarItem(title, icon, selected){
                    tabNavigator.current = tab
                }
            }
        }
    }

    @Composable
    private fun RowScope.BottomBarItem(
        title: String,
        icon: Painter,
        selected: Boolean,
        onClick: () -> Unit
    ) {
        val color = if (selected) Colors.PRIMARY else Colors.SECONDARY_TEXT
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(vertical = 16.dp)
                .noRippleClickable(onClick),
            verticalArrangement = Arrangement.spacedBy(5.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                modifier = Modifier.size(24.dp),
                painter = icon,
                contentDescription = title,
                tint = color
            )
            Text(
                text = title,
                color = color,
                style = Typography.S,
                maxLines = 1,
                overflow = TextOverflow.Clip
            )
        }
    }


    @Composable
    private fun CurrentTabTransition(tabNavigator: TabNavigator, tabs: List<Tab>) {
        AnimatedContent(
            targetState = tabNavigator.current,
            transitionSpec = {
                val initialIndex = tabs.indexOf(initialState).coerceAtLeast(0)
                val targetIndex = tabs.indexOf(targetState).coerceAtLeast(0)

                if (targetIndex > initialIndex) {
                    slideInHorizontally(
                        animationSpec = tween(200),
                        initialOffsetX = { it }
                    ) togetherWith slideOutHorizontally(
                        animationSpec = tween(200),
                        targetOffsetX = { -it }
                    )
                } else {
                    slideInHorizontally(
                        animationSpec = tween(200),
                        initialOffsetX = { -it }
                    ) togetherWith slideOutHorizontally(
                        animationSpec = tween(200),
                        targetOffsetX = { it }
                    )
                }
            }
        ) { tab ->
            tab.Content()
        }
    }


}