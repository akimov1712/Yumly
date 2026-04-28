package ru.topbun.navigation

import cafe.adriel.voyager.core.registry.ScreenProvider

sealed interface BmiScreenProvider : ScreenProvider {

    data object Main : BmiScreenProvider

}
