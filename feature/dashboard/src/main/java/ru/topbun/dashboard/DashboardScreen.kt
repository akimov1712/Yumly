package ru.topbun.dashboard

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.screen.Screen

object DashboardScreen: Screen{
    @Composable
    override fun Content() {
        Text("Dashboard")
    }
}