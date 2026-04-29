package ru.topbun.core.ui.utils

import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.isImeVisible
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun Modifier.useBottomBarPadding(bottomBarVisible: Boolean = true) = this.padding(
    bottom = getBottomBarPadding(bottomBarVisible)
)

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun getBottomBarPadding(bottomBarVisible: Boolean = true): Dp {
    val bottom = WindowInsets.systemBars
        .asPaddingValues()
        .calculateBottomPadding()
    return if (WindowInsets.isImeVisible || !bottomBarVisible) 24.dp + bottom else LocalBottomBarPadding.current
}