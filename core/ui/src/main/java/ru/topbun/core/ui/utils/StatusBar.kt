package ru.topbun.core.ui.utils

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.graphics.Color
import com.google.accompanist.systemuicontroller.rememberSystemUiController

enum class StatusBarColor{
    LIGHT, DARK
}

@Composable
fun changeStatusBarColor(color: StatusBarColor){
    val systemUiController = rememberSystemUiController()
    DisposableEffect(systemUiController, color) {
        systemUiController.setSystemBarsColor(
            color = Color.Transparent,
            darkIcons = color == StatusBarColor.DARK
        )
        onDispose {}
    }
}