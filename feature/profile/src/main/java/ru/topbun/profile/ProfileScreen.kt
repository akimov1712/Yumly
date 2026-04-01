package ru.topbun.profile

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabOptions

object ProfileScreen: Tab {

    override val options @Composable get() = TabOptions(
        index = 4U,
        title = "Profile",
        icon = painterResource(ru.topbun.core.ui.R.drawable.ic_tab_profile)
    )

    @Composable
    override fun Content() {

    }
}