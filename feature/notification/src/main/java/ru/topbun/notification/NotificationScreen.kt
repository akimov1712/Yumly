package ru.topbun.notification

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabOptions

object NotificationScreen: Tab {

    override val options @Composable get() = TabOptions(
        index = 3U,
        title = "Notification",
        icon = painterResource(ru.topbun.core.ui.R.drawable.ic_tabs_notification)
    )

    @Composable
    override fun Content() {
    }
}