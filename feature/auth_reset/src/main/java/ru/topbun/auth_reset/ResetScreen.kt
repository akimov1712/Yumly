package ru.topbun.auth_reset

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import cafe.adriel.voyager.core.screen.Screen
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf
import ru.topbun.auth_reset.components.Description
import ru.topbun.auth_reset.components.FieldPassword
import ru.topbun.auth_reset.components.Title
import ru.topbun.core.ui.components.Height
import ru.topbun.core.ui.theme.Colors

data class ResetScreen(private val email: String): Screen{

    @Composable
    override fun Content() {
        val viewModel: ResetViewModel = koinViewModel { parametersOf(email) }
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
                modifier = Modifier.verticalScroll(rememberScrollState())
                    .padding(24.dp, 64.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Title()
                Height(8.dp)
                Description()
                Height(32.dp)
                FieldPassword(
                    value = state.password,
                    placeholder = "Password",
                    isShowPassword = state.showPassword,
                    onClickShowPassword = { viewModel.sendIntent(ResetIntent.SwitchShowPassword) },
                    onChangeValue = { viewModel.sendIntent(ResetIntent.ChangePassword(it)) }
                )
                Height(16.dp)
                FieldPassword(
                    value = state.confirmPassword,
                    placeholder = "Confirm password",
                    isShowPassword = state.showPassword,
                    onClickShowPassword = { viewModel.sendIntent(ResetIntent.SwitchShowPassword) },
                    onChangeValue = { viewModel.sendIntent(ResetIntent.ChangeConfirmPassword(it)) }
                )
            }
        }
    }

}
