package ru.topbun.auth_login.components

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ru.topbun.core.ui.theme.Colors
import ru.topbun.core.ui.theme.Typography
import ru.topbun.core.ui.utils.noRippleClickable

@Composable
internal fun ColumnScope.ForgotPasswordButton(onClick: () -> Unit) {
    Text(
        modifier = Modifier
            .align(Alignment.End)
            .noRippleClickable { onClick() }
            .padding(vertical = 4.dp, horizontal = 8.dp),
        text = "Забыли пароль?",
        style = Typography.P2,
        color = Colors.PRIMARY,
    )
}
