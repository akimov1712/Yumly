package ru.topbun.upload.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import ru.topbun.core.ui.components.AppTextButton
import ru.topbun.core.ui.components.Weight
import ru.topbun.core.ui.theme.Colors
import ru.topbun.core.ui.theme.Typography
import ru.topbun.upload.fragments.UploadFragments

@Composable
internal fun Header(
    selectedFragment: UploadFragments,
    fragments: List<UploadFragments>,
    onClickClear: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        AppTextButton(
            text = "Очистить",
            containerColor = Colors.SECONDARY,
            textColor = Colors.SECONDARY
        ) { onClickClear() }
        Weight(1f)
        Text(
            text = buildAnnotatedString {
                val order = fragments.indexOf(selectedFragment) + 1
                append("$order/")
                withStyle(SpanStyle(color = Colors.SECONDARY_TEXT)) { append(fragments.size.toString()) }
            },
            style = Typography.H2,
            color = Colors.MAIN_TEXT
        )
    }
}