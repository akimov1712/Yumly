package ru.topbun.feature.splash

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ru.topbun.core.ui.components.PulseLoading
import ru.topbun.core.ui.theme.Colors
import ru.topbun.core.ui.utils.StatusBarColor
import ru.topbun.core.ui.utils.changeStatusBarColor

@Preview
@Composable
fun SplashScreen(modifier: Modifier = Modifier) {

    changeStatusBarColor(StatusBarColor.LIGHT)

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Colors.PRIMARY)
            .statusBarsPadding(),
        contentAlignment = Alignment.Center
    ) {
        Logo()
        Loader()
    }
}

@Composable
private fun Logo() {
    Icon(
        modifier = Modifier.size(164.dp),
        painter = painterResource(ru.topbun.core.ui.R.drawable.ic_logo),
        contentDescription = "Logo",
        tint = Colors.WHITE
    )
}

@Composable
private fun Loader() {
    Column {
        Spacer(Modifier.fillMaxHeight(0.5f))
        PulseLoading()
    }
}