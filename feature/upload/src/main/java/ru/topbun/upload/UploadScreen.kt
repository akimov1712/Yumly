package ru.topbun.upload

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabOptions
import org.koin.compose.viewmodel.koinViewModel
import ru.topbun.core.ui.R
import ru.topbun.core.ui.components.Height
import ru.topbun.core.ui.theme.Colors
import ru.topbun.core.ui.utils.LocalBottomBarPadding
import ru.topbun.upload.components.ClearDataDialog
import ru.topbun.upload.components.Header
import ru.topbun.upload.fragments.BasicFragment

object UploadScreen: Tab {

    override val options @Composable get() = TabOptions(
        index = 1U,
        title = "Upload",
        icon = painterResource(R.drawable.ic_tabs_upload)
    )

    @Composable
    override fun Content() {
        val context = LocalContext.current
        val viewModel: UploadViewModel = koinViewModel()
        val state by viewModel.state.collectAsState()

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(Colors.BACKGROUND)
                .verticalScroll(rememberScrollState())
                .systemBarsPadding()
                .padding(top = 12.dp, bottom = LocalBottomBarPadding.current)
                .padding(horizontal = 12.dp)
        ) {
            Header(
                selectedOrder = state.selectedOrderFragments,
                fragmentsSize = state.fragments.size,
                onClickClear = { viewModel.sendIntent(UploadIntent.ChangeShowDialogClearData(true)) },
            )
            Height(24.dp)
            BasicFragment()
        }

        if (state.showDialogClearData) {
            ClearDataDialog(
                onDismissRequest = { viewModel.sendIntent(UploadIntent.ChangeShowDialogClearData(false)) },
                onClickConfirm = {
                    viewModel.sendIntent(UploadIntent.ChangeShowDialogClearData(false))
                    viewModel.sendIntent(UploadIntent.ClearData)
                    Toast.makeText(context, "Данные успешно очищены", Toast.LENGTH_SHORT).show()
                }
            )
        }
    }
}
