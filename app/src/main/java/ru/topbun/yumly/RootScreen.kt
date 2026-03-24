package ru.topbun.yumly

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.transitions.ScaleTransition
import org.koin.compose.koinInject
import org.koin.compose.viewmodel.koinViewModel
import ru.topbun.core.android.SnackbarManager
import ru.topbun.core.ui.components.AppSnackbarHost
import ru.topbun.core.ui.theme.Colors
import ru.topbun.core.ui.utils.ObserveAsEvents
import ru.topbun.feature.splash.SplashScreen

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun RootScreen() {

    val snackbarManager = koinInject<SnackbarManager>()
    val snackbarHostState = remember { SnackbarHostState() }

    ObserveAsEvents(snackbarManager.messages) {
        snackbarHostState.showSnackbar(it)
    }

    Box {
        Surface(
            contentColor = Colors.MAIN_TEXT,
            color = Colors.WHITE
        ) {
            Navigator(SplashScreen) {
                ScaleTransition(it)
            }
        }
        AppSnackbarHost(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .statusBarsPadding()
                .padding(24.dp),
            snackbarHostState = snackbarHostState
        )
    }
}