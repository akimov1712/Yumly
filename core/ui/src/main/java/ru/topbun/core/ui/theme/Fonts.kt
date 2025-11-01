package ru.topbun.core.ui.theme

import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import ru.topbun.core.ui.R

object Fonts {

    private fun createFont(vararg fontToWeight: Pair<Int, FontWeight>) = FontFamily(
        fontToWeight.map { Font(it.first, it.second) }
    )

    val INTER: FontFamily
        get() = createFont(
            R.font.medium to FontWeight.Medium,
            R.font.bold to FontWeight.Bold
        )

}