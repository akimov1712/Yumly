package ru.topbun.recipe.components

import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import ru.topbun.core.ui.R
import ru.topbun.core.ui.components.AppButton
import ru.topbun.core.ui.theme.Colors

@Composable
internal fun CookCtaButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    AppButton(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp)
            .defaultMinSize(minHeight = 56.dp),
        text = "Начать готовить",
        startIcon = {
            Icon(
                modifier = Modifier.size(18.dp),
                painter = painterResource(R.drawable.ic_time),
                contentDescription = null,
                tint = Colors.WHITE
            )
        },
        onClick = onClick
    )
}
