package ru.topbun.auth

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import cafe.adriel.voyager.core.registry.ScreenRegistry
import cafe.adriel.voyager.core.registry.rememberScreen
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.transitions.SlideTransition
import ru.topbun.core.ui.utils.StatusBarColor
import ru.topbun.core.ui.utils.changeStatusBarColor
import ru.topbun.navigation.auth.AuthConfirmMode
import ru.topbun.navigation.auth.AuthScreenProvider
import ru.topbun.navigation.auth.AuthStartScreen

data class AuthScreen(
    private val startDestination: AuthStartScreen
) : Screen {

    @Composable
    override fun Content() {
        changeStatusBarColor(StatusBarColor.DARK)

        val initialScreen = remember(startDestination) {
            when (startDestination) {
                AuthStartScreen.WELCOME -> ScreenRegistry.get(AuthScreenProvider.Welcome)
                AuthStartScreen.LOGIN -> ScreenRegistry.get(AuthScreenProvider.Login)
            }
        }

        Navigator(initialScreen){
            SlideTransition(it)
        }
    }

}