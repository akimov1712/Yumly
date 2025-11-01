package ru.topbun.yumly

import androidx.compose.runtime.Composable
import ru.topbun.core.ui.utils.StatusBarColor
import ru.topbun.core.ui.utils.changeStatusBarColor
import ru.topbun.feature.splash.SplashScreen

@Composable
fun AppScreen() {
    changeStatusBarColor(StatusBarColor.DARK)
    SplashScreen()
}