package ru.topbun.feature.auth.welcome

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import cafe.adriel.voyager.core.registry.ScreenRegistry
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import org.koin.compose.viewmodel.koinViewModel
import ru.topbun.core.ui.utils.ObserveAsEvents
import ru.topbun.navigation.auth.AuthScreenProvider

object WelcomeScreen : Screen {

    @Composable
    override fun Content() {


    }

}
