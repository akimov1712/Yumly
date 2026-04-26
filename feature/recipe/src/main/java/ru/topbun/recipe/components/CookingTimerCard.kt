package ru.topbun.recipe.components

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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.topbun.core.ui.R
import ru.topbun.core.ui.components.Width
import ru.topbun.core.ui.theme.Colors
import ru.topbun.core.ui.theme.Fonts
import ru.topbun.core.ui.theme.Typography
import ru.topbun.core.ui.utils.rippleClickable

@Composable
internal fun CookingTimerCard(
    secondsLeft: Int,
    isPaused: Boolean,
    progress: Float,
    onPauseToggle: () -> Unit,
    onReset: () -> Unit,
    onStop: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .dropShadow(
                shape = RoundedCornerShape(28.dp),
                shadow = Shadow(
                    radius = 12.dp,
                    alpha = 0.18f,
                    offset = DpOffset(0.dp, 6.dp)
                )
            )
            .clip(RoundedCornerShape(28.dp))
            .background(Colors.MAIN_TEXT)
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(Colors.PRIMARY.copy(0.18f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    modifier = Modifier.size(20.dp),
                    painter = painterResource(R.drawable.ic_time),
                    contentDescription = null,
                    tint = Colors.PRIMARY
                )
            }
            Width(12.dp)
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = if (isPaused) "Готовка на паузе" else "Идёт приготовление",
                    style = Typography.S,
                    color = Colors.WHITE.copy(0.65f)
                )
                Text(
                    text = formatTimer(secondsLeft),
                    color = Colors.WHITE,
                    fontFamily = Fonts.INTER,
                    fontWeight = FontWeight.Bold,
                    fontSize = 28.sp
                )
            }
            CircleButton(
                iconRes = R.drawable.ic_close,
                tintBg = Colors.WHITE.copy(0.12f),
                tintIcon = Colors.WHITE,
                onClick = onStop
            )
        }
        ProgressTrack(progress = progress)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            ActionButton(
                modifier = Modifier.weight(1f),
                text = if (isPaused) "Продолжить" else "Пауза",
                onClick = onPauseToggle,
                primary = isPaused
            )
            ActionButton(
                modifier = Modifier.weight(1f),
                text = "Сбросить",
                onClick = onReset,
                primary = false
            )
        }
    }
}

@Composable
private fun ProgressTrack(progress: Float) {
    val animated by animateFloatAsState(
        targetValue = progress.coerceIn(0f, 1f),
        animationSpec = tween(500),
        label = "timer_progress"
    )
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(6.dp)
            .clip(RoundedCornerShape(4.dp))
            .background(Colors.WHITE.copy(0.15f))
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
private fun ActionButton(
    modifier: Modifier = Modifier,
    text: String,
    onClick: () -> Unit,
    primary: Boolean,
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(24.dp))
            .background(
                if (primary) Colors.PRIMARY else Colors.WHITE.copy(0.12f)
            )
            .rippleClickable(onClick = onClick)
            .padding(vertical = 12.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            style = Typography.H3,
            color = Colors.WHITE
        )
    }
}

@Composable
private fun CircleButton(
    iconRes: Int,
    tintBg: androidx.compose.ui.graphics.Color,
    tintIcon: androidx.compose.ui.graphics.Color,
    onClick: () -> Unit,
) {
    IconButton(
        modifier = Modifier
            .size(40.dp)
            .clip(CircleShape)
            .background(tintBg),
        onClick = onClick
    ) {
        Icon(
            modifier = Modifier.size(18.dp),
            painter = painterResource(iconRes),
            contentDescription = null,
            tint = tintIcon
        )
    }
}

private fun formatTimer(secondsLeft: Int): String {
    val s = secondsLeft.coerceAtLeast(0)
    val h = s / 3600
    val m = (s % 3600) / 60
    val sec = s % 60
    return if (h > 0) {
        "%d:%02d:%02d".format(h, m, sec)
    } else {
        "%02d:%02d".format(m, sec)
    }
}
