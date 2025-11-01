package ru.topbun.core.ui.theme

import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

object Typography {

    val H1 = TextStyle(
        fontFamily = Fonts.INTER,
        fontWeight = FontWeight.Bold,
        fontSize = 22.sp,
        lineHeight = 32.sp,
        color = Colors.MAIN_TEXT
    )

    val H2 = TextStyle(
        fontFamily = Fonts.INTER,
        fontWeight = FontWeight.Bold,
        fontSize = 17.sp,
        lineHeight = 27.sp,
        color = Colors.MAIN_TEXT
    )

    val H3 = TextStyle(
        fontFamily = Fonts.INTER,
        fontWeight = FontWeight.Bold,
        fontSize = 15.sp,
        lineHeight = 25.sp,
        color = Colors.MAIN_TEXT
    )

    val P1 = TextStyle(
        fontFamily = Fonts.INTER,
        fontWeight = FontWeight.Medium,
        fontSize = 17.sp,
        lineHeight = 27.sp,
        color = Colors.MAIN_TEXT
    )

    val P2 = TextStyle(
        fontFamily = Fonts.INTER,
        fontWeight = FontWeight.Medium,
        fontSize = 15.sp,
        lineHeight = 25.sp,
        color = Colors.MAIN_TEXT
    )

    val S = TextStyle(
        fontFamily = Fonts.INTER,
        fontWeight = FontWeight.Medium,
        fontSize = 12.sp,
        lineHeight = 15.sp,
        color = Colors.MAIN_TEXT
    )

}