package ru.topbun.auth_login

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import ru.topbun.core.ui.theme.Colors

object LoginScreen : Screen {
    @Composable
    override fun Content() {
        Scaffold(
            snackbarHost = {
                Snackbar() { }
            }
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Colors.WHITE)
                    .systemBarsPadding()
                    .padding(it)
                    .verticalScroll(rememberScrollState())
                    .padding(vertical = 24.dp),
            ) {

            }
        }
    }
}