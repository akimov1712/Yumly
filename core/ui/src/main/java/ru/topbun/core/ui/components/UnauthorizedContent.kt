package ru.topbun.core.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import ru.topbun.core.ui.R
import ru.topbun.core.ui.theme.Colors
import ru.topbun.core.ui.theme.Typography

@Composable
fun UnauthorizedSection(onClick: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 32.dp)
            .systemBarsPadding(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            modifier = Modifier.size(160.dp),
            painter = painterResource(R.drawable.img_unauthorized),
            contentDescription = null,
        )
        Height(32.dp)
        Text(
            text = "Вы не авторизованы",
            style = Typography.H1,
            color = Colors.MAIN_TEXT
        )
        Height(8.dp)
        Text(
            text = "Войдите в аккаунт, чтобы продолжить",
            style = Typography.P2,
            color = Colors.MAIN_TEXT
        )
        Height(24.dp)
        AppButton(
            modifier = Modifier.padding(horizontal = 24.dp).fillMaxWidth().heightIn(min = 56.dp),
            text = "Войти",
            onClick = onClick
        )
    }
}
