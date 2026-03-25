package ru.topbun.auth_login

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.rememberScrollableState
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
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import org.koin.compose.viewmodel.koinViewModel
import ru.topbun.auth_login.components.Description
import ru.topbun.auth_login.components.FieldEmail
import ru.topbun.auth_login.components.FieldPassword
import ru.topbun.auth_login.components.ForgotPasswordButton
import ru.topbun.auth_login.components.LoginButton
import ru.topbun.auth_login.components.SignUpButton
import ru.topbun.auth_login.components.Title
import ru.topbun.core.ui.components.Height
import ru.topbun.core.ui.theme.Colors
import ru.topbun.core.ui.utils.ObserveAsEvents
import ru.topbun.navigation.auth.AuthScreenProvider

object LoginScreen : Screen {

    @Composable
    override fun Content() {
        val viewModel: LoginViewModel = koinViewModel()
        val state by viewModel.state.collectAsState()
        val navigator = LocalNavigator.currentOrThrow

        ObserveAsEvents(viewModel.events) {
            when(it){
                LoginEvent.NavigateToSignUp -> {
                    val screen = ScreenRegistry.get(AuthScreenProvider.Register)
                    navigator.push(screen)
                }
                LoginEvent.NavigateToResetPassword -> {}
            }
        }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Colors.WHITE)
                .systemBarsPadding()
                .imePadding(),
            contentAlignment = Alignment.Center
        ){
            Column(
                modifier = Modifier.verticalScroll(rememberScrollState())
                    .padding(24.dp, 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Title()
                Height(8.dp)
                Description()
                Height(32.dp)
                FieldEmail(state.email){ viewModel.sendIntent(LoginIntent.ChangeEmail(it)) }
                Height(16.dp)
                FieldPassword(state.password, state.showPassword){ viewModel.sendIntent(it) }
                Height(12.dp)
                ForgotPasswordButton{ viewModel.sendIntent(LoginIntent.ClickResetPassword) }
                Height(72.dp)
                LoginButton(
                    enabled = state.loginButtonEnabled,
                    isLoading = state.loginIsLoading
                ){ viewModel.sendIntent(LoginIntent.ClickLogin) }
            }
            SignUpButton{ viewModel.sendIntent(LoginIntent.ClickSingUp) }
        }
    }

}



