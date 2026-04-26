package ru.topbun.upload.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.dropShadow
import androidx.compose.ui.graphics.shadow.Shadow
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import ru.topbun.core.ui.R
import ru.topbun.core.ui.components.AppButton
import ru.topbun.core.ui.components.Height
import ru.topbun.core.ui.theme.Colors
import ru.topbun.core.ui.theme.Typography
import ru.topbun.core.ui.utils.rippleClickable

@Composable
internal fun Header(
    selectedOrder: Int,
    fragmentsSize: Int,
    showBackButton: Boolean,
    publishButtonEnabled: Boolean,
    publishButtonLoading: Boolean,
    onClickClear: () -> Unit,
    onClickBack: () -> Unit,
    onClickPublish: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 8.dp, bottom = 12.dp),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            if (showBackButton) {
                CircleIconButton(
                    iconRes = R.drawable.ic_back,
                    onClick = onClickBack
                )
            } else {
                CircleIconButton(
                    iconRes = R.drawable.ic_close,
                    tint = Colors.SECONDARY,
                    onClick = onClickClear
                )
            }
            Column(
                modifier = Modifier.weight(1f),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Новый рецепт",
                    style = Typography.H2,
                    color = Colors.MAIN_TEXT
                )
                Text(
                    text = stepSubtitle(selectedOrder),
                    style = Typography.S,
                    color = Colors.SECONDARY_TEXT
                )
            }
            if (showBackButton) {
                AppButton(
                    text = "Опубликовать",
                    enabled = publishButtonEnabled,
                    isLoading = publishButtonLoading,
                    onClick = onClickPublish
                )
            } else {
                StepCounter(
                    selectedOrder = selectedOrder,
                    fragmentsSize = fragmentsSize
                )
            }
        }
        Height(14.dp)
        StepProgress(
            selectedOrder = selectedOrder,
            fragmentsSize = fragmentsSize
        )
    }
}

private fun stepSubtitle(selectedOrder: Int): String = when (selectedOrder) {
    1 -> "Шаг 1 — основное"
    2 -> "Шаг 2 — состав"
    else -> "Шаг $selectedOrder"
}

@Composable
private fun StepCounter(
    selectedOrder: Int,
    fragmentsSize: Int,
) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(20.dp))
            .background(Colors.PRIMARY.copy(0.12f))
            .padding(horizontal = 14.dp, vertical = 8.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = buildAnnotatedString {
                withStyle(SpanStyle(color = Colors.PRIMARY)) {
                    append(selectedOrder.toString())
                }
                append(" / $fragmentsSize")
            },
            style = Typography.H3,
            color = Colors.MAIN_TEXT
        )
    }
}

@Composable
private fun StepProgress(
    selectedOrder: Int,
    fragmentsSize: Int,
) {
    val target = if (fragmentsSize == 0) 0f else selectedOrder.toFloat() / fragmentsSize
    val animated by animateFloatAsState(
        targetValue = target.coerceIn(0f, 1f),
        animationSpec = tween(350),
        label = "step_progress"
    )
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(6.dp)
            .clip(RoundedCornerShape(4.dp))
            .background(Colors.OUTLINE.copy(0.5f))
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth(animated)
                .height(6.dp)
                .clip(RoundedCornerShape(4.dp))
                .background(Colors.PRIMARY)
        )
    }
}

@Composable
private fun CircleIconButton(
    iconRes: Int,
    onClick: () -> Unit,
    tint: androidx.compose.ui.graphics.Color = Colors.MAIN_TEXT,
) {
    IconButton(
        modifier = Modifier
            .size(44.dp)
            .dropShadow(
                shape = CircleShape,
                shadow = Shadow(radius = 4.dp, alpha = 0.08f)
            )
            .clip(CircleShape)
            .background(Colors.WHITE)
            .rippleClickable(onClick = onClick),
        onClick = onClick
    ) {
        Icon(
            modifier = Modifier.size(20.dp),
            painter = painterResource(iconRes),
            contentDescription = null,
            tint = tint
        )
    }
}
