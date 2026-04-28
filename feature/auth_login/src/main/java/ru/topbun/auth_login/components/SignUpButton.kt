package ru.topbun.auth_login.components

import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import ru.topbun.core.ui.theme.Colors
import ru.topbun.core.ui.theme.Typography
import ru.topbun.core.ui.utils.noRippleClickable

@Composable
internal fun BoxScope.SignUpButton(onClick: () -> Unit) {
    Text(
        modifier = Modifier
            .padding(24.dp)
            .align(Alignment.BottomCenter)
            .noRippleClickable { onClick() }
            .padding(vertical = 4.dp, horizontal = 8.dp),
        text = buildAnnotatedString {
            append("Нет аккаунта? ")
            withStyle(SpanStyle(color = Colors.PRIMARY)) {
                append("Зарегистрироваться")
            }
        },
        style = Typography.P2,
        color = Colors.MAIN_TEXT,
        textAlign = TextAlign.Center
    )
}
