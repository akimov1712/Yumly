package ru.topbun.upload.components

import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil3.compose.rememberAsyncImagePainter
import org.koin.compose.koinInject
import ru.topbun.core.android.SnackbarManager
import ru.topbun.core.ui.R
import ru.topbun.core.ui.components.Height
import ru.topbun.core.ui.theme.Colors
import ru.topbun.core.ui.theme.Typography
import ru.topbun.core.ui.utils.ImagePickerHelper
import ru.topbun.core.ui.utils.rippleClickable

private val PreviewCornerRadius = 28.dp

@Composable
internal fun PreviewPicker(
    previewUri: Uri?,
    onChangePreview: (Uri?) -> Unit
) {
    val snackbarManager = koinInject<SnackbarManager>()
    val context = LocalContext.current
    val launcher = ImagePickerHelper.createImagePickerLauncher(
        context = context,
        onImagePicked = { onChangePreview(it) },
        onError = { snackbarManager.showMessage(it) }
    )

    val shape = RoundedCornerShape(PreviewCornerRadius)
    val baseModifier = Modifier
        .fillMaxWidth()
        .aspectRatio(1.6f)
        .clip(shape)

    val placeholderModifier = baseModifier
        .background(Colors.PRIMARY.copy(alpha = 0.06f))
        .dashedBorder(
            color = Colors.PRIMARY.copy(alpha = 0.6f),
            cornerRadius = PreviewCornerRadius,
            strokeWidth = 1.5.dp,
            dashOn = 8.dp,
            dashOff = 6.dp,
        )
        .rippleClickable(Colors.PRIMARY) { ImagePickerHelper.launchPicker(launcher) }

    val imageModifier = baseModifier
        .rippleClickable(Colors.BLACK) { ImagePickerHelper.launchPicker(launcher) }

    Box(
        modifier = if (previewUri == null) placeholderModifier else imageModifier,
        contentAlignment = Alignment.Center
    ) {
        if (previewUri != null) {
            PreviewImage(previewUri) { onChangePreview(null) }
        } else {
            Placeholder()
        }
    }
}

@Composable
private fun BoxScope.PreviewImage(
    previewUri: Uri,
    onClearPreview: () -> Unit
) {
    Image(
        modifier = Modifier.fillMaxSize(),
        painter = rememberAsyncImagePainter(previewUri),
        contentDescription = null,
        contentScale = ContentScale.Crop
    )
    IconButton(
        modifier = Modifier
            .align(Alignment.TopEnd)
            .padding(24.dp)
            .clip(CircleShape)
            .background(Colors.BLACK.copy(0.35f)),
        onClick = { onClearPreview() }
    ) {
        Icon(
            modifier = Modifier.size(24.dp),
            painter = painterResource(R.drawable.ic_close),
            contentDescription = null,
            tint = Colors.WHITE
        )
    }
}

@Composable
private fun Placeholder() {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(72.dp)
                .clip(CircleShape)
                .background(Colors.PRIMARY.copy(alpha = 0.14f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                modifier = Modifier.size(36.dp),
                painter = painterResource(R.drawable.ic_image_picker),
                contentDescription = "image_picker",
                tint = Colors.PRIMARY
            )
        }
        Height(16.dp)
        Text(
            text = "Добавить фото обложки",
            color = Colors.MAIN_TEXT,
            style = Typography.H3
        )
        Height(6.dp)
        Text(
            text = "JPG / PNG, до 8 Mb",
            color = Colors.SECONDARY_TEXT,
            style = Typography.S
        )
    }
}

private fun Modifier.dashedBorder(
    color: Color,
    cornerRadius: Dp,
    strokeWidth: Dp,
    dashOn: Dp,
    dashOff: Dp,
): Modifier = drawBehind {
    val sw = strokeWidth.toPx()
    val innerCorner = (cornerRadius.toPx() - sw / 2f).coerceAtLeast(0f)
    val stroke = Stroke(
        width = sw,
        pathEffect = PathEffect.dashPathEffect(
            floatArrayOf(dashOn.toPx(), dashOff.toPx()),
            0f
        )
    )
    drawRoundRect(
        color = color,
        topLeft = Offset(sw / 2f, sw / 2f),
        size = Size(size.width - sw, size.height - sw),
        cornerRadius = CornerRadius(innerCorner, innerCorner),
        style = stroke,
    )
}
