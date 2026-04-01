package ru.topbun.navigation

import cafe.adriel.voyager.core.registry.ScreenProvider

sealed interface DashboardScreenProvider: ScreenProvider {

    object Home: DashboardScreenProvider
    object Upload: DashboardScreenProvider
    object Assistant: DashboardScreenProvider
    object Notification: DashboardScreenProvider
    object Profile: DashboardScreenProvider

}