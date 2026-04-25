package ru.topbun.profile_settings

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import org.koin.compose.viewmodel.koinViewModel
import ru.topbun.core.ui.components.Height
import ru.topbun.core.ui.theme.Colors
import ru.topbun.core.ui.utils.ObserveAsEvents
import ru.topbun.profile_settings.components.AvatarPicker
import ru.topbun.profile_settings.components.Header
import ru.topbun.profile_settings.components.SaveButton
import ru.topbun.profile_settings.components.UsernameField

object ProfileSettingsScreen : Screen {

    @Composable
    override fun Content() {
        val viewModel: ProfileSettingsViewModel = koinViewModel()
        val state by viewModel.state.collectAsState()
        val navigator = LocalNavigator.currentOrThrow

        LaunchedEffect(Unit) {
            viewModel.sendIntent(ProfileSettingsIntent.LoadAccount)
        }

        ObserveAsEvents(viewModel.events) { event ->
            when (event) {
                ProfileSettingsEvent.Saved -> navigator.pop()
            }
        }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Colors.BACKGROUND)
                .systemBarsPadding()
                .imePadding()
        ) {
            Column(modifier = Modifier.fillMaxSize()) {
                Header(onClickBack = { navigator.pop() })

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .verticalScroll(rememberScrollState())
                        .padding(horizontal = 24.dp, vertical = 16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    AvatarPicker(
                        photoUri = state.photoUri,
                        photoUrl = state.photoUrl,
                        onPick = { viewModel.sendIntent(ProfileSettingsIntent.ChangePhoto(it)) },
                        onClear = { viewModel.sendIntent(ProfileSettingsIntent.ClearPhoto) }
                    )
                    Height(28.dp)
                    UsernameField(
                        value = state.username,
                        onValueChange = {
                            viewModel.sendIntent(ProfileSettingsIntent.ChangeUsername(it))
                        }
                    )
                }

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp, vertical = 16.dp)
                ) {
                    SaveButton(
                        enabled = state.canSave,
                        isLoading = state.saveStatus.isLoading,
                        onClick = { viewModel.sendIntent(ProfileSettingsIntent.Save) }
                    )
                }
            }
        }
    }
}
