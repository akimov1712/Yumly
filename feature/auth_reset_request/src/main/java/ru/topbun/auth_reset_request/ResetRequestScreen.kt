package ru.topbun.auth_reset_request

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
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
import cafe.adriel.voyager.core.registry.ScreenRegistry
import cafe.adriel.voyager.core.screen.Screen
import org.koin.compose.viewmodel.koinViewModel
import ru.topbun.auth_reset_request.components.Description
import ru.topbun.auth_reset_request.components.FieldEmail
import ru.topbun.auth_reset_request.components.ResetButton
import ru.topbun.auth_reset_request.components.Title
import ru.topbun.core.ui.components.Height
import ru.topbun.core.ui.theme.Colors
import ru.topbun.core.ui.utils.ObserveAsEvents
import ru.topbun.navigation.auth.AuthConfirmMode
import ru.topbun.navigation.auth.AuthScreenProvider

object ResetRequestScreen: Screen {

    @Composable
    override fun Content() {
        val viewModel: ResetRequestViewModel = koinViewModel()
        val state by viewModel.state.collectAsState()

        ObserveAsEvents(viewModel.events) {
            when(it){
                is ResetRequestEvent.NavigateToConfirm -> {
                    val screen = ScreenRegistry.get(AuthScreenProvider.Confirm(it.email, AuthConfirmMode.RESET_PASSWORD))
                }
            }
        }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Colors.WHITE)
                .systemBarsPadding()
                .imePadding(),
            contentAlignment = Alignment.Center
        ) {
            Column(
                modifier = Modifier.verticalScroll(rememberScrollState())
                    .padding(24.dp, 64.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Title()
                Height(8.dp)
                Description()
                Height(32.dp)
                FieldEmail(state.email) { viewModel.sendIntent(ResetRequestIntent.ChangeEmail(it)) }
                Height(32.dp)
                ResetButton(
                    enabled = state.buttonIsEnabled,
                    isLoading = state.isLoading
                ) { viewModel.sendIntent(ResetRequestIntent.ClickReset) }
            }
        }

    }

}