package ru.topbun.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import cafe.adriel.voyager.core.registry.ScreenRegistry
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabOptions
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf
import ru.topbun.core.ui.R
import ru.topbun.core.ui.components.UnauthorizedSection
import ru.topbun.core.ui.theme.Colors
import ru.topbun.core.ui.utils.ObserveAsEvents
import ru.topbun.navigation.ProfileScreenProvider
import ru.topbun.navigation.RootScreenProvider
import ru.topbun.navigation.auth.AuthScreenProvider
import ru.topbun.navigation.auth.AuthStartScreen
import ru.topbun.navigation.utills.root
import ru.topbun.profile.ProfileState.ProfileUiState.NEED_AUTH
import ru.topbun.profile.ProfileState.ProfileUiState.SUCCESS
import ru.topbun.profile.components.ProfileContent

object ProfileScreen : Tab {

    override val options @Composable get() = TabOptions(
        index = 4U,
        title = "Profile",
        icon = painterResource(R.drawable.ic_tab_profile)
    )

    @Composable
    override fun Content() {
        val viewModel: ProfileViewModel = koinViewModel { parametersOf(ProfileState.Mode.Self) }
        val state by viewModel.state.collectAsState()
        val navigator = LocalNavigator.currentOrThrow.parent

        LaunchedEffect(Unit) {
            viewModel.sendIntent(ProfileIntent.CheckSession)
        }

        ObserveAsEvents(viewModel.events) { event ->
            when (event) {
                ProfileEvent.LoggedOut -> {
                    val authScreen = ScreenRegistry.get(
                        RootScreenProvider.Auth(AuthStartScreen.LOGIN)
                    )
                    navigator?.root()?.replaceAll(authScreen)
                }
                ProfileEvent.NavigateToAuth -> {
                    val screen = ScreenRegistry.get(AuthScreenProvider.Login)
                    navigator?.push(screen)
                }
            }
        }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Colors.BACKGROUND)
                .statusBarsPadding()
        ) {
            when (state.profileUiState) {
                SUCCESS -> ProfileContent(
                    viewModel = viewModel,
                    showBack = false,
                    onBack = {},
                    onNavigateToSettings = {
                        val screen = ScreenRegistry.get(ProfileScreenProvider.Settings)
                        navigator?.push(screen)
                    }
                )
                NEED_AUTH -> UnauthorizedSection {
                    val screen = ScreenRegistry.get(AuthScreenProvider.Login)
                    navigator?.push(screen)
                }
                else -> Unit
            }
        }
    }
}
