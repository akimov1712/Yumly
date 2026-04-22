package ru.topbun.core.ui.utils

import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.isImeVisible
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun Modifier.useBottomBarPadding() = this.padding(
    bottom = getBottomBarPadding()
)

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun getBottomBarPadding() = if (WindowInsets.isImeVisible) 24.dp else LocalBottomBarPadding.current