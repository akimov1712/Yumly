package ru.topbun.recipe.components

import androidx.compose.animation.animateColorAsState
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
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.topbun.core.ui.R
import ru.topbun.core.ui.components.Width
import ru.topbun.core.ui.theme.Colors
import ru.topbun.core.ui.theme.Fonts
import ru.topbun.core.ui.theme.Typography
import ru.topbun.core.ui.utils.rippleClickable
import ru.topbun.recipe.RecipeState

private val MIN_TIMER_SECONDS = 0
private val MAX_TIMER_SECONDS = 6 * 60 * 60
private val STEP_SECONDS = 30

@Composable
internal fun TimerSection(
    timer: RecipeState.TimerState,
    onChangeMode: (RecipeState.TimerMode) -> Unit,
    onChangeTarget: (Int) -> Unit,
    onStart: () -> Unit,
    onPause: () -> Unit,
    onReset: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(28.dp))
            .background(Colors.WHITE)
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                modifier = Modifier.weight(1f),
                text = "Таймер",
                style = Typography.H2,
                color = Colors.MAIN_TEXT
            )
        }

        ModeSwitch(
            mode = timer.mode,
            enabled = !timer.isRunning,
            onChangeMode = onChangeMode
        )

        TimerDisplay(
            secondsLeft = timer.displaySeconds,
            mode = timer.mode,
            isRunning = timer.isRunning,
            progress = timer.progress
        )

        if (timer.mode == RecipeState.TimerMode.Timer) {
            TargetAdjuster(
                targetSeconds = timer.targetSeconds,
                enabled = !timer.isRunning,
                onChangeTarget = onChangeTarget
            )
        }

        Controls(
            timer = timer,
            onStart = onStart,
            onPause = onPause,
            onReset = onReset
        )
    }
}

@Composable
private fun ModeSwitch(
    mode: RecipeState.TimerMode,
    enabled: Boolean,
    onChangeMode: (RecipeState.TimerMode) -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(28.dp))
            .background(Colors.FORM)
            .padding(4.dp)
            .alpha(if (enabled) 1f else 0.6f),
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        RecipeState.TimerMode.entries.forEach { current ->
            ModeTab(
                modifier = Modifier.weight(1f),
                title = current.title,
                selected = current == mode,
                onClick = { if (enabled) onChangeMode(current) }
            )
        }
    }
}

@Composable
private fun ModeTab(
    modifier: Modifier,
    title: String,
    selected: Boolean,
    onClick: () -> Unit,
) {
    val backgroundColor by animateColorAsState(
        targetValue = if (selected) Colors.PRIMARY else Color.Transparent,
        animationSpec = tween(200),
        label = "timer_tab_bg"
    )
    val textColor by animateColorAsState(
        targetValue = if (selected) Colors.WHITE else Colors.SECONDARY_TEXT,
        animationSpec = tween(200),
        label = "timer_tab_text"
    )
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(24.dp))
            .background(backgroundColor)
            .rippleClickable(color = Colors.PRIMARY, onClick = onClick)
            .padding(vertical = 10.dp, horizontal = 8.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = title,
            style = Typography.S.copy(fontWeight = FontWeight.Bold),
            color = textColor,
            maxLines = 1
        )
    }
}

@Composable
private fun TimerDisplay(
    secondsLeft: Int,
    mode: RecipeState.TimerMode,
    isRunning: Boolean,
    progress: Float,
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = formatTimer(secondsLeft),
            color = Colors.MAIN_TEXT,
            fontFamily = Fonts.INTER,
            fontWeight = FontWeight.Bold,
            fontSize = 44.sp
        )
        Text(
            text = when {
                isRunning && mode == RecipeState.TimerMode.Timer -> "Идёт отсчёт"
                isRunning && mode == RecipeState.TimerMode.Stopwatch -> "Идёт замер"
                mode == RecipeState.TimerMode.Timer -> "Установите время и нажмите старт"
                else -> "Нажмите старт, чтобы начать замер"
            },
            style = Typography.S,
            color = Colors.SECONDARY_TEXT
        )
        if (mode == RecipeState.TimerMode.Timer) {
            ProgressTrack(progress = progress)
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
            .height(8.dp)
            .clip(RoundedCornerShape(4.dp))
            .background(Colors.PRIMARY.copy(0.15f))
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth(animated)
                .height(8.dp)
                .clip(RoundedCornerShape(4.dp))
                .background(Colors.PRIMARY)
        )
    }
}

@Composable
private fun TargetAdjuster(
    targetSeconds: Int,
    enabled: Boolean,
    onChangeTarget: (Int) -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(Colors.FORM)
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        AdjustButton(
            iconRes = R.drawable.ic_minus,
            enabled = enabled && targetSeconds - STEP_SECONDS >= MIN_TIMER_SECONDS,
            onClick = { onChangeTarget((targetSeconds - STEP_SECONDS).coerceAtLeast(MIN_TIMER_SECONDS)) }
        )
        Column(
            modifier = Modifier.weight(1f),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Длительность",
                style = Typography.S,
                color = Colors.SECONDARY_TEXT
            )
            Text(
                text = formatTimerVerbose(targetSeconds),
                style = Typography.H3,
                color = Colors.MAIN_TEXT
            )
        }
        AdjustButton(
            iconRes = R.drawable.ic_plus,
            enabled = enabled && targetSeconds + STEP_SECONDS <= MAX_TIMER_SECONDS,
            onClick = { onChangeTarget((targetSeconds + STEP_SECONDS).coerceAtMost(MAX_TIMER_SECONDS)) }
        )
    }
}

@Composable
private fun AdjustButton(
    iconRes: Int,
    enabled: Boolean,
    onClick: () -> Unit,
) {
    val color = if (enabled) Colors.PRIMARY else Colors.PRIMARY.copy(0.4f)
    IconButton(
        modifier = Modifier
            .size(40.dp)
            .clip(CircleShape)
            .background(color),
        onClick = onClick,
        enabled = enabled
    ) {
        Icon(
            modifier = Modifier.size(16.dp),
            painter = painterResource(iconRes),
            contentDescription = null,
            tint = Colors.WHITE
        )
    }
}

@Composable
private fun Controls(
    timer: RecipeState.TimerState,
    onStart: () -> Unit,
    onPause: () -> Unit,
    onReset: () -> Unit,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        ActionButton(
            modifier = Modifier.weight(1f),
            text = if (timer.isRunning) "Пауза" else "Старт",
            iconRes = if (timer.isRunning) R.drawable.ic_minus else R.drawable.ic_time,
            enabled = timer.canStart,
            primary = true,
            onClick = if (timer.isRunning) onPause else onStart
        )
        ActionButton(
            modifier = Modifier.weight(1f),
            text = "Сбросить",
            iconRes = R.drawable.ic_close,
            enabled = timer.elapsedSeconds > 0 || timer.isRunning,
            primary = false,
            onClick = onReset
        )
    }
}

@Composable
private fun ActionButton(
    modifier: Modifier,
    text: String,
    iconRes: Int,
    enabled: Boolean,
    primary: Boolean,
    onClick: () -> Unit,
) {
    val containerColor = if (primary) Colors.PRIMARY else Colors.FORM
    val contentColor = if (primary) Colors.WHITE else Colors.MAIN_TEXT
    Row(
        modifier = modifier
            .clip(RoundedCornerShape(24.dp))
            .background(if (enabled) containerColor else containerColor.copy(0.5f))
            .rippleClickable(enabled = enabled, onClick = onClick)
            .padding(vertical = 14.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            modifier = Modifier.size(16.dp),
            painter = painterResource(iconRes),
            contentDescription = null,
            tint = contentColor
        )
        Width(8.dp)
        Text(
            text = text,
            style = Typography.H3,
            color = contentColor
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

private fun formatTimerVerbose(seconds: Int): String {
    val s = seconds.coerceAtLeast(0)
    if (s == 0) return "0 мин"
    val h = s / 3600
    val m = (s % 3600) / 60
    val sec = s % 60
    return buildString {
        if (h > 0) append("$h ч ")
        if (m > 0) append("$m мин ")
        if (sec > 0) append("$sec с")
    }.trim()
}
