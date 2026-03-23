package ru.topbun.yumly

import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.transitions.ScaleTransition
import ru.topbun.core.ui.theme.Colors
import ru.topbun.feature.splash.SplashScreen

@Composable
fun RootScreen() {
    Surface(
        contentColor = Colors.MAIN_TEXT,
        color = Colors.WHITE
    ){
        Navigator(SplashScreen){
            ScaleTransition(it)
        }
    }
}