package ru.topbun.core.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight

object Fonts {

    private fun createFont(vararg fontToWeight: Pair<Int, FontWeight>) = FontFamily(
        fontToWeight.map { Font(it.first, it.second) }
    )

    val INTER: FontFamily
        @Composable get() = createFont(
            R.font.medium to FontWeight.Medium,
            R.font.bold to FontWeight.Bold
        )

}