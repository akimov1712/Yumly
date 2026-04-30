package ru.topbun.auth_register

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
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import org.koin.compose.viewmodel.koinViewModel
import ru.topbun.auth_register.components.AgreementSection
import ru.topbun.auth_register.components.Description
import ru.topbun.auth_register.components.FieldEmail
import ru.topbun.auth_register.components.FieldPassword
import ru.topbun.auth_register.components.FieldUsername
import ru.topbun.auth_register.components.LoginButton
import ru.topbun.auth_register.components.RegisterButton
import ru.topbun.auth_register.components.Title
import ru.topbun.core.ui.components.Height
import ru.topbun.core.ui.theme.Colors
import ru.topbun.core.ui.utils.ObserveAsEvents
import ru.topbun.navigation.auth.AuthConfirmMode
import ru.topbun.navigation.auth.AuthScreenProvider

object RegisterScreen: Screen {

    @Composable
    override fun Content() {
        val viewModel: RegisterViewModel = koinViewModel()
        val state by viewModel.state.collectAsState()
        val navigator = LocalNavigator.currentOrThrow

        ObserveAsEvents(viewModel.events) {
            when(it){
                RegisterEvent.NavigateToLogin -> {
                    val screen = ScreenRegistry.get(AuthScreenProvider.Login)
                    val screenStack = navigator.items
                    val previousScreenLogin = if (screenStack.size >= 2) screenStack.get(screenStack.size - 2) == screen else false
                    if (previousScreenLogin){
                        navigator.pop()
                    } else {
                        navigator.push(screen)
                    }
                }

                is RegisterEvent.NavigateToConfirm -> {
                    val screen = ScreenRegistry.get(AuthScreenProvider.Confirm(it.email, AuthConfirmMode.SIGN_UP))
                    navigator.push(screen)
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
        ){
            Column(
                modifier = Modifier.verticalScroll(rememberScrollState())
                    .padding(24.dp, 64.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Title()
                Height(8.dp)
                Description()
                Height(32.dp)
                FieldUsername(
                    value = state.username,
                    error = state.usernameError,
                    onFocused = { viewModel.sendIntent(RegisterIntent.ChangeFieldFocused(RegisterState.FieldFocused.USERNAME)) }
                ){ viewModel.sendIntent(RegisterIntent.ChangeUsername(it)) }
                Height(16.dp)
                FieldEmail(
                    value = state.email,
                    error = state.emailError,
                    onFocused = { viewModel.sendIntent(RegisterIntent.ChangeFieldFocused(RegisterState.FieldFocused.EMAIL)) }
                ){ viewModel.sendIntent(RegisterIntent.ChangeEmail(it)) }
                Height(16.dp)
                FieldPassword(
                    value = state.password,
                    placeholder = "Пароль",
                    isShowPassword = state.showPassword,
                    error = state.passwordError,
                    onFocused = { viewModel.sendIntent(RegisterIntent.ChangeFieldFocused(RegisterState.FieldFocused.PASSWORD)) },
                    onClickShowPassword = { viewModel.sendIntent(RegisterIntent.SwitchShowPassword) },
                    onChangeValue = { viewModel.sendIntent(RegisterIntent.ChangePassword(it)) }
                )
                Height(16.dp)
                FieldPassword(
                    value = state.confirmPassword,
                    placeholder = "Подтвердите пароль",
                    isShowPassword = state.showPassword,
                    error = state.confirmPasswordError,
                    onFocused = { viewModel.sendIntent(RegisterIntent.ChangeFieldFocused(RegisterState.FieldFocused.CONFIRM_PASSWORD)) },
                    onClickShowPassword = { viewModel.sendIntent(RegisterIntent.SwitchShowPassword) },
                    onChangeValue = { viewModel.sendIntent(RegisterIntent.ChangeConfirmPassword(it)) }
                )
                Height(18.dp)
                AgreementSection(
                    checked = state.isAgreementAccepted,
                    onCheckedChange = {
                        viewModel.sendIntent(RegisterIntent.ChangeAgreementAccepted(it))
                    },
                )
                Height(40.dp)
                RegisterButton(
                    enabled = state.registerButtonEnabled,
                    isLoading = state.registerIsLoading
                ){ viewModel.sendIntent(RegisterIntent.ClickSignUp) }
            }
            LoginButton {
                viewModel.sendIntent(RegisterIntent.ClickLogin)
            }
        }
    }

}
