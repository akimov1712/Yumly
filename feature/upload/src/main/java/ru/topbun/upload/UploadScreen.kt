package ru.topbun.upload

import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.registry.ScreenRegistry
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabOptions
import org.koin.compose.viewmodel.koinViewModel
import ru.topbun.core.ui.R
import ru.topbun.core.ui.components.UnauthorizedSection
import ru.topbun.core.ui.theme.Colors
import ru.topbun.core.ui.utils.LocalBottomBarPadding
import ru.topbun.navigation.auth.AuthScreenProvider
import ru.topbun.upload.UploadState.UploadUiState.NEED_AUTH
import ru.topbun.upload.UploadState.UploadUiState.SUCCESS
import ru.topbun.upload.components.ClearDataDialog
import ru.topbun.upload.components.Header
import ru.topbun.upload.components.SuccessPublishRecipeDialog
import ru.topbun.upload.fragments.BasicFragment
import ru.topbun.upload.fragments.ContentFragment
import ru.topbun.upload.fragments.UploadFragments.Basic
import ru.topbun.upload.fragments.UploadFragments.Content

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
        val navigator = LocalNavigator.currentOrThrow.parent
        val isContentFragmentSelected = state.selectedFragment == Content

        LaunchedEffect(Unit) {
            viewModel.sendIntent(UploadIntent.CheckSession)
        }

        BackHandler(enabled = isContentFragmentSelected) {
            viewModel.sendIntent(UploadIntent.ChangeFragment(Basic))
        }

        when(state.uploadUiState){
            SUCCESS -> UploadContent()
            NEED_AUTH -> UnauthorizedSection {
                val screen = ScreenRegistry.get(AuthScreenProvider.Login)
                navigator?.push(screen)
            }
            else -> {}
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

        state.publishedRecipeId?.let {
            SuccessPublishRecipeDialog(
                onDismissRequest = { viewModel.sendIntent(UploadIntent.ChangeShowDialogSuccessPublish(null)) },
                onClickOpenRecipe = {  }
            )
        }
    }
}

@Composable
private fun UploadContent(
    viewModel: UploadViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsState()
    val isContentFragmentSelected = state.selectedFragment == Content
    Column(
        modifier = Modifier.fillMaxWidth()
            .systemBarsPadding()
            .padding(horizontal = 12.dp)
    ){
        Header(
            selectedOrder = state.selectedOrderFragments,
            fragmentsSize = state.fragments.size,
            showBackButton = isContentFragmentSelected,
            publishButtonEnabled = state.publishButtonEnabled,
            publishButtonLoading = state.publishLoading,
            onClickClear = { viewModel.sendIntent(UploadIntent.ChangeShowDialogClearData(true)) },
            onClickBack = { viewModel.sendIntent(UploadIntent.ChangeFragment(Basic)) },
            onClickPublish = { viewModel.sendIntent(UploadIntent.PublishRecipe) }
        )
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Colors.BACKGROUND)
                .verticalScroll(rememberScrollState())
                .padding(bottom = LocalBottomBarPadding.current)
        ) {
            when (state.selectedFragment) {
                Basic -> BasicFragment()
                Content -> ContentFragment()
            }
        }
    }

}
