package ru.topbun.auth_login

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import ru.topbun.auth_login.components.Description
import ru.topbun.auth_login.components.FieldEmail
import ru.topbun.auth_login.components.FieldPassword
import ru.topbun.auth_login.components.ForgotPasswordButton
import ru.topbun.auth_login.components.LoginButton
import ru.topbun.auth_login.components.SignUpButton
import ru.topbun.auth_login.components.Title
import ru.topbun.core.ui.components.AppButton
import ru.topbun.core.ui.components.Height
import ru.topbun.core.ui.components.Weight
import ru.topbun.core.ui.theme.Colors
import ru.topbun.core.ui.theme.Typography
import ru.topbun.core.ui.utils.noRippleClickable

object LoginScreen : Screen {

    @Composable
    override fun Content() {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Colors.WHITE)
                .systemBarsPadding()
                .imePadding(),
            contentAlignment = Alignment.Center
        ){
            Column(
                modifier = Modifier.padding(24.dp, 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Title()
                Height(8.dp)
                Description()
                Height(32.dp)
                FieldEmail()
                Height(16.dp)
                FieldPassword()
                Height(12.dp)
                ForgotPasswordButton()
                Height(72.dp)
                LoginButton()
            }
            SignUpButton()
        }
    }

}



