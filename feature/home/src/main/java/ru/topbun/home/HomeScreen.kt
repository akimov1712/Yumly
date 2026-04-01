package ru.topbun.home

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabOptions

object HomeScreen: Tab {

    override val options @Composable get() = TabOptions(
        index = 0U,
        title = "Home",
        icon = painterResource(ru.topbun.core.ui.R.drawable.ic_tabs_home)
    )

    @Composable
    override fun Content() {
    }


}