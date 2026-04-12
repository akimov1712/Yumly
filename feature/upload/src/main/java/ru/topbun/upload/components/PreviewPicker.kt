package ru.topbun.upload.components

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
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

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(1.6f)
            .clip(RoundedCornerShape(28.dp))
            .border(1.dp, Colors.SECONDARY_TEXT.copy(0.5f), RoundedCornerShape(28.dp))
            .rippleClickable(Colors.BLACK) { ImagePickerHelper.launchPicker(launcher) },
        contentAlignment = Alignment.Center
    ){
        if (previewUri != null){
            PreviewImage(previewUri){ onChangePreview(null) }
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
        modifier = Modifier.align(Alignment.TopEnd)
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
        Icon(
            modifier = Modifier.size(60.dp),
            painter = painterResource(R.drawable.ic_image_picker),
            contentDescription = "image_picker",
            tint = Colors.SECONDARY_TEXT
        )
        Height(16.dp)
        Text(
            text = "Добавить обложку",
            color = Colors.MAIN_TEXT,
            style = Typography.H3
        )
        Height(10.dp)
        Text(
            text = "(до 12 Mb)",
            color = Colors.SECONDARY_TEXT,
            style = Typography.S
        )
    }
}