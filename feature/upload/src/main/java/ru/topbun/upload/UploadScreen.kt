package ru.topbun.upload

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabOptions

object UploadScreen: Tab {

    override val options @Composable get() = TabOptions(
        index = 1U,
        title = "Upload",
        icon = painterResource(ru.topbun.core.ui.R.drawable.ic_tabs_upload)
    )

    @Composable
    override fun Content() {
    }
}