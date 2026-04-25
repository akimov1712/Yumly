package ru.topbun.profile_settings.components

import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil3.compose.rememberAsyncImagePainter
import org.koin.compose.koinInject
import ru.topbun.core.android.SnackbarManager
import ru.topbun.core.ui.R
import ru.topbun.core.ui.components.AppAsyncImage
import ru.topbun.core.ui.theme.Colors
import ru.topbun.core.ui.utils.ImagePickerHelper
import ru.topbun.core.ui.utils.rippleClickable

@Composable
internal fun AvatarPicker(
    photoUri: Uri?,
    photoUrl: String?,
    onPick: (Uri?) -> Unit,
    onClear: () -> Unit,
) {
    val snackbarManager = koinInject<SnackbarManager>()
    val context = LocalContext.current
    val launcher = ImagePickerHelper.createImagePickerLauncher(
        context = context,
        onImagePicked = { onPick(it) },
        onError = { snackbarManager.showMessage(it) }
    )

    val hasPhoto = photoUri != null || !photoUrl.isNullOrBlank()

    Box(
        modifier = Modifier
            .size(140.dp)
            .clip(CircleShape)
            .background(Colors.FORM)
            .border(2.dp, Colors.OUTLINE.copy(0.5f), CircleShape)
            .rippleClickable(Colors.BLACK) { ImagePickerHelper.launchPicker(launcher) },
        contentAlignment = Alignment.Center
    ) {
        when {
            photoUri != null -> Image(
                modifier = Modifier.fillMaxSize(),
                painter = rememberAsyncImagePainter(photoUri),
                contentDescription = null,
                contentScale = ContentScale.Crop
            )
            !photoUrl.isNullOrBlank() -> AppAsyncImage(
                modifier = Modifier.fillMaxSize(),
                url = photoUrl,
                contentScale = ContentScale.Crop
            )
            else -> Icon(
                modifier = Modifier.fillMaxSize().padding(36.dp),
                painter = painterResource(R.drawable.ic_user),
                contentDescription = null,
                tint = Colors.SECONDARY_TEXT
            )
        }

        Box(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(4.dp)
                .size(36.dp)
                .clip(CircleShape)
                .background(Colors.PRIMARY),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                modifier = Modifier.size(18.dp),
                painter = painterResource(R.drawable.ic_camera),
                contentDescription = null,
                tint = Colors.WHITE
            )
        }

        if (hasPhoto) {
            IconButton(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .size(32.dp)
                    .clip(CircleShape)
                    .background(Color.Black.copy(0.4f)),
                onClick = onClear
            ) {
                Icon(
                    modifier = Modifier.size(18.dp),
                    painter = painterResource(R.drawable.ic_close),
                    contentDescription = null,
                    tint = Colors.WHITE
                )
            }
        }
    }
}
