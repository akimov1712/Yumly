package ru.topbun.core.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import ru.topbun.core.ui.R
import ru.topbun.core.ui.theme.Colors

@Composable
fun ZoomableImageDialog(
    url: String?,
    onDismissRequest: () -> Unit,
) {
    if (url.isNullOrBlank()) return

    Dialog(
        onDismissRequest = onDismissRequest,
        properties = DialogProperties(
            usePlatformDefaultWidth = false,
            decorFitsSystemWindows = false,
            dismissOnBackPress = true,
            dismissOnClickOutside = false
        )
    ) {
        var scale by remember { mutableFloatStateOf(1f) }
        var offsetX by remember { mutableFloatStateOf(0f) }
        var offsetY by remember { mutableFloatStateOf(0f) }
        var containerSize by remember { mutableStateOf(IntSize.Zero) }

        val animatedScale by animateFloatAsState(
            targetValue = scale,
            animationSpec = tween(180),
            label = "zoom_scale"
        )
        val animatedOffsetX by animateFloatAsState(
            targetValue = offsetX,
            animationSpec = tween(180),
            label = "zoom_offset_x"
        )
        val animatedOffsetY by animateFloatAsState(
            targetValue = offsetY,
            animationSpec = tween(180),
            label = "zoom_offset_y"
        )

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Colors.BLACK.copy(alpha = 0.95f))
                .onSizeChanged { containerSize = it }
                .pointerInput(Unit) {
                    detectTapGestures(
                        onTap = { onDismissRequest() },
                        onDoubleTap = { tap ->
                            if (scale > 1f) {
                                scale = 1f
                                offsetX = 0f
                                offsetY = 0f
                            } else {
                                scale = 2.5f
                                val centerX = containerSize.width / 2f
                                val centerY = containerSize.height / 2f
                                offsetX = ((centerX - tap.x) * (scale - 1f))
                                    .coerceMaxOffset(containerSize.width, scale)
                                offsetY = ((centerY - tap.y) * (scale - 1f))
                                    .coerceMaxOffset(containerSize.height, scale)
                            }
                        }
                    )
                }
                .pointerInput(Unit) {
                    detectTransformGestures { _, pan, zoom, _ ->
                        val newScale = (scale * zoom).coerceIn(1f, 5f)
                        val newOffsetX = (offsetX + pan.x)
                            .coerceMaxOffset(containerSize.width, newScale)
                        val newOffsetY = (offsetY + pan.y)
                            .coerceMaxOffset(containerSize.height, newScale)
                        scale = newScale
                        offsetX = if (newScale == 1f) 0f else newOffsetX
                        offsetY = if (newScale == 1f) 0f else newOffsetY
                    }
                },
            contentAlignment = Alignment.Center
        ) {
            AppAsyncImage(
                modifier = Modifier
                    .fillMaxSize()
                    .graphicsLayer(
                        scaleX = animatedScale,
                        scaleY = animatedScale,
                        translationX = animatedOffsetX,
                        translationY = animatedOffsetY,
                    ),
                url = url,
                contentScale = ContentScale.Fit
            )

            IconButton(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .statusBarsPadding()
                    .padding(12.dp)
                    .size(44.dp)
                    .clip(CircleShape)
                    .background(Colors.WHITE.copy(alpha = 0.15f)),
                onClick = onDismissRequest
            ) {
                Icon(
                    modifier = Modifier.size(20.dp),
                    painter = painterResource(R.drawable.ic_close),
                    contentDescription = null,
                    tint = Colors.WHITE
                )
            }
        }
    }
}

private fun Float.coerceMaxOffset(containerSide: Int, scale: Float): Float {
    if (scale <= 1f || containerSide == 0) return 0f
    val maxOffset = containerSide * (scale - 1f) / 2f
    return this.coerceIn(-maxOffset, maxOffset)
}
