package ru.topbun.recipe.components

import android.util.Log
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImagePainter
import ru.topbun.core.ui.R
import ru.topbun.core.ui.components.AppAsyncImage
import ru.topbun.core.ui.components.AppTextButton
import ru.topbun.core.ui.components.Height
import ru.topbun.core.ui.components.ZoomableImageDialog
import ru.topbun.core.ui.theme.Colors
import ru.topbun.core.ui.theme.Fonts
import ru.topbun.core.ui.theme.Typography
import ru.topbun.core.ui.utils.rippleClickable
import ru.topbun.domain.entity.recipe.StepEntity

@Composable
internal fun StepsSection(
    steps: List<StepEntity>,
    completedSteps: Set<Int>,
    progress: Float,
    onToggleStep: (index: Int) -> Unit,
    onClickReset: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(28.dp))
            .background(Colors.WHITE)
            .padding(top = 10.dp, bottom = 20.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .defaultMinSize(minHeight = 48.dp)
                .padding(horizontal = 20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                modifier = Modifier.weight(1f),
                text = buildAnnotatedString {
                    append("Шаги ")
                    withStyle(SpanStyle(color = Colors.SECONDARY_TEXT)) {
                        append("(${steps.size})")
                    }
                },
                style = Typography.H2,
                color = Colors.MAIN_TEXT
            )
            if (completedSteps.isNotEmpty()) {
                AppTextButton(
                    modifier = Modifier.defaultMinSize(minHeight = 48.dp),
                    text = "Сбросить",
                    containerColor = Colors.SECONDARY_TEXT,
                    textColor = Colors.SECONDARY_TEXT,
                    onClick = onClickReset
                )
            }
        }
        Height(12.dp)
        StepsProgress(
            progress = progress,
            completedCount = completedSteps.size,
            total = steps.size
        )
        Height(16.dp)
        Column(
            modifier = Modifier.padding(horizontal = 12.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            steps.forEachIndexed { index, step ->
                StepCard(
                    position = index + 1,
                    step = step,
                    completed = completedSteps.contains(index),
                    onToggle = { onToggleStep(index) }
                )
            }
        }
    }
}

@Composable
private fun StepsProgress(
    progress: Float,
    completedCount: Int,
    total: Int,
) {
    val animatedProgress by animateFloatAsState(
        targetValue = progress.coerceIn(0f, 1f),
        animationSpec = tween(400),
        label = "steps_progress"
    )
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp),
        verticalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                modifier = Modifier.weight(1f),
                text = "Готовка по шагам",
                style = Typography.S,
                color = Colors.SECONDARY_TEXT
            )
            Text(
                text = buildAnnotatedString {
                    withStyle(SpanStyle(color = Colors.PRIMARY)) {
                        append("$completedCount")
                    }
                    append(" из $total")
                },
                style = Typography.S,
                color = Colors.SECONDARY_TEXT
            )
        }
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(8.dp)
                .clip(RoundedCornerShape(4.dp))
                .background(Colors.PRIMARY.copy(0.15f))
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(animatedProgress)
                    .height(8.dp)
                    .clip(RoundedCornerShape(4.dp))
                    .background(Colors.PRIMARY)
            )
        }
    }
}

@Composable
private fun StepCard(
    position: Int,
    step: StepEntity,
    completed: Boolean,
    onToggle: () -> Unit,
) {
    var zoomOpen by remember { mutableStateOf(false) }
    val accent = if (completed) Colors.PRIMARY else Colors.BLUE_TEXT
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(if (completed) Colors.PRIMARY.copy(0.06f) else Colors.FORM)
            .rippleClickable(color = Colors.PRIMARY, onClick = onToggle)
            .padding(12.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .background(accent),
                contentAlignment = Alignment.Center
            ) {
                if (completed) {
                    Icon(
                        modifier = Modifier.size(14.dp),
                        painter = painterResource(R.drawable.ic_checkmark),
                        contentDescription = null,
                        tint = Colors.WHITE
                    )
                } else {
                    Text(
                        text = position.toString(),
                        color = Colors.WHITE,
                        fontFamily = Fonts.INTER,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp
                    )
                }
            }
        }
        Column(
            modifier = Modifier
                .weight(1f)
                .alpha(if (completed) 0.6f else 1f),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = step.description,
                style = Typography.P2.copy(
                    textDecoration = if (completed) TextDecoration.LineThrough else TextDecoration.None
                ),
                color = Colors.MAIN_TEXT
            )
            if (!step.previewUrl.isNullOrBlank()){
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(1.7f)
                        .clip(RoundedCornerShape(16.dp))
                        .background(Colors.OUTLINE.copy(0.3f))
                        .rippleClickable(color = Colors.PRIMARY) { zoomOpen = true }
                ) {
                    AppAsyncImage(
                        modifier = Modifier.fillMaxSize(),
                        url = step.previewUrl,
                        contentScale = ContentScale.Crop,
                        onState = {
                            if (it is AsyncImagePainter.State.Error){
                                Log.d("STEPS_PREVIEW_LOAD_ERROR", "$it - " + it.result.throwable.message ?: "Неизвестно")
                            }
                        }
                    )
                }
            }
        }
    }

    if (zoomOpen) {
        ZoomableImageDialog(
            url = step.previewUrl,
            onDismissRequest = { zoomOpen = false }
        )
    }
}
