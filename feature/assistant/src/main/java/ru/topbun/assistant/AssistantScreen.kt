package ru.topbun.assistant

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabOptions

object AssistantScreen: Tab {


    override val options @Composable get() = TabOptions(
        index = 2U,
        title = "Assistant",
        icon = painterResource(ru.topbun.core.ui.R.drawable.ic_tabs_assistant)
    )

    @Composable
    override fun Content() {
    }


}