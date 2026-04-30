package ru.topbun.profile_settings.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import ru.topbun.core.ui.R
import ru.topbun.core.ui.theme.Colors
import ru.topbun.core.ui.theme.Typography
import ru.topbun.core.ui.utils.rippleClickable

@Composable
internal fun LegalDocumentsSection(
    onClickPrivacyPolicy: () -> Unit,
    onClickUserAgreement: () -> Unit,
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = "Документы",
            style = Typography.H3,
            color = Colors.MAIN_TEXT
        )
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(24.dp))
                .background(Colors.WHITE)
        ) {
            LegalDocumentItem(
                iconRes = R.drawable.ic_privacy,
                title = "Политика конфиденциальности",
                onClick = onClickPrivacyPolicy
            )
            LegalDocumentItem(
                iconRes = R.drawable.ic_document,
                title = "Пользовательское соглашение",
                onClick = onClickUserAgreement
            )
        }
    }
}

@Composable
private fun LegalDocumentItem(
    iconRes: Int,
    title: String,
    onClick: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .rippleClickable(onClick = onClick)
            .padding(horizontal = 20.dp, vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Icon(
            modifier = Modifier.size(24.dp),
            painter = painterResource(iconRes),
            contentDescription = null,
            tint = Colors.BLUE_TEXT
        )
        Text(
            text = title,
            style = Typography.P1,
            color = Colors.MAIN_TEXT
        )
    }
}
