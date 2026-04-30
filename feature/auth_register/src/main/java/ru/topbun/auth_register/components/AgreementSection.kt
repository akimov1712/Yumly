package ru.topbun.auth_register.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.LinkAnnotation
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withLink
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import ru.topbun.core.android.LegalLinks
import ru.topbun.core.ui.theme.Colors
import ru.topbun.core.ui.theme.Typography

@Composable
internal fun AgreementSection(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.Top
    ) {
        Checkbox(
            checked = checked,
            onCheckedChange = onCheckedChange,
            colors = CheckboxDefaults.colors(
                checkedColor = Colors.PRIMARY,
                uncheckedColor = Colors.OUTLINE,
                checkmarkColor = Colors.WHITE
            )
        )
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(top = 10.dp, start = 4.dp)
        ) {
            Text(
                text = buildAnnotatedString {
                    append("Создавая аккаунт, вы соглашаетесь с ")
                    withLink(LinkAnnotation.Url(LegalLinks.PRIVACY_POLICY_URL)) {
                        withStyle(SpanStyle(color = Colors.PRIMARY)) {
                            append("Политикой конфиденциальности")
                        }
                    }
                    append(" и ")
                    withLink(LinkAnnotation.Url(LegalLinks.USER_AGREEMENT_URL)) {
                        withStyle(SpanStyle(color = Colors.PRIMARY)) {
                            append("Пользовательским соглашением")
                        }
                    }
                },
                style = Typography.P2,
                color = Colors.MAIN_TEXT
            )
        }
    }
}
