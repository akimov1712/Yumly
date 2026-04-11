package ru.topbun.upload

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabOptions
import ru.topbun.core.ui.R
import ru.topbun.core.ui.components.Height
import ru.topbun.core.ui.theme.Colors
import ru.topbun.core.ui.utils.LocalBottomBarPadding
import ru.topbun.upload.components.Header
import ru.topbun.upload.UploadFragments
import ru.topbun.upload.basic.BasicScreen

object UploadScreen: Tab {

    override val options @Composable get() = TabOptions(
        index = 1U,
        title = "Upload",
        icon = painterResource(R.drawable.ic_tabs_upload)
    )

    @Composable
    override fun Content() {
        Column(
            modifier = Modifier.fillMaxWidth()
                .background(Colors.BACKGROUND)
                .verticalScroll(rememberScrollState())
                .systemBarsPadding()
                .padding(top = 12.dp, bottom = LocalBottomBarPadding.current)
                .padding(horizontal = 12.dp)
        ) {
            Header(
                selectedFragment = UploadFragments.Basic,
                fragments = UploadFragments.entries,
                onClickClear = {}
            )
            Height(24.dp)
            BasicScreen()
        }
    }
}
