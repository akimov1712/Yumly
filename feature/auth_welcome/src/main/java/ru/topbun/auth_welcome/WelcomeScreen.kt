package ru.topbun.auth_welcome

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.registry.ScreenRegistry
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import kotlinx.coroutines.flow.Flow
import org.koin.compose.viewmodel.koinViewModel
import ru.topbun.auth_welcome.components.Button
import ru.topbun.auth_welcome.components.OnboardingImage
import ru.topbun.auth_welcome.components.SkipButton
import ru.topbun.auth_welcome.components.TitleWithDescription
import ru.topbun.core.ui.components.Height
import ru.topbun.core.ui.components.Weight
import ru.topbun.core.ui.theme.Colors
import ru.topbun.core.ui.utils.ObserveAsEvents
import ru.topbun.navigation.RootScreenProvider
import ru.topbun.navigation.auth.AuthScreenProvider
import ru.topbun.navigation.utills.root

object WelcomeScreen : Screen {

    @Composable
    override fun Content() {
        val viewModel: WelcomeViewModel = koinViewModel()

        ObserveEvents(viewModel.events)

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Colors.WHITE)
                .systemBarsPadding()
                .padding(vertical = 24.dp)
        ) {
            SkipButton { viewModel.sendIntent(WelcomeIntent.Skip) }
            Height(30.dp)
            OnboardingImage()
            Height(30.dp)
            TitleWithDescription()
            Weight(1f)
            Button { viewModel.sendIntent(WelcomeIntent.Continue) }
        }

    }

    @Composable
    private fun ObserveEvents(events: Flow<WelcomeEvent>) {
        val navigator = LocalNavigator.currentOrThrow
        ObserveAsEvents(events) {
            when(it){
                WelcomeEvent.NavigateToLogin -> {
                    val loginScreen = ScreenRegistry.get(AuthScreenProvider.Login)
                    navigator.push(loginScreen)
                }
                WelcomeEvent.NavigateToDashboard -> {
                    val dashboardScreen = ScreenRegistry.get(RootScreenProvider.Dashboard)
                    navigator.root().replaceAll(dashboardScreen)
                }
            }
        }
    }


}
