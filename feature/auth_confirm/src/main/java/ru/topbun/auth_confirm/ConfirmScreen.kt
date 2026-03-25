package ru.topbun.auth_confirm

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf
import ru.topbun.auth_confirm.components.Description
import ru.topbun.auth_confirm.components.FilledButton
import ru.topbun.auth_confirm.components.Title
import ru.topbun.core.ui.components.AppOutlinedButton
import ru.topbun.core.ui.components.Height
import ru.topbun.core.ui.components.OtpInput
import ru.topbun.core.ui.theme.Colors
import ru.topbun.navigation.auth.AuthConfirmMode
import ru.topbun.navigation.auth.AuthConfirmMode.RESET_PASSWORD
import ru.topbun.navigation.auth.AuthConfirmMode.SIGN_UP

data class ConfirmScreen(
    private val email: String,
    private val screenMode: AuthConfirmMode
) : Screen {

    @Composable
    override fun Content() {
        val viewModel: ConfirmViewModel = koinViewModel { parametersOf(email, screenMode) }
        val state by viewModel.state.collectAsState()

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Colors.WHITE)
                .systemBarsPadding()
                .imePadding(),
            contentAlignment = Alignment.Center
        ) {
            Column(
                modifier = Modifier
                    .verticalScroll(rememberScrollState())
                    .padding(24.dp, 64.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Title()
                Height(8.dp)
                Description()
                Height(32.dp)
                OtpInput { viewModel.sendIntent(ConfirmIntent.ChangeCode(it)) }
                Height(72.dp)
                FilledButton(
                    text = when (state.screenMode) {
                        SIGN_UP -> "Verify"
                        RESET_PASSWORD -> "Next"
                    },
                    enabled = state.confirmEnabled,
                    isLoading = state.confirmLoading,
                ) { viewModel.sendIntent(ConfirmIntent.ClickConfirm) }
                Height(16.dp)
                AppOutlinedButton(
                    text = "Send again " + if (!state.sendAgainEnabled) state.formatTimer() else "",
                    modifier = Modifier
                        .fillMaxWidth()
                        .defaultMinSize(minHeight = 56.dp),
                    enabled = state.sendAgainEnabled,
                    isLoading = state.sendAgainLoading
                ) { viewModel.sendIntent(ConfirmIntent.ClickSendAgain) }
            }
        }
    }

}