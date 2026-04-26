package ru.topbun.profile_settings.components

import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
    var showActionsDialog by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .size(140.dp)
            .clip(CircleShape)
            .background(Colors.FORM)
            .border(2.dp, Colors.OUTLINE.copy(0.5f), CircleShape)
            .rippleClickable(Colors.BLACK) { showActionsDialog = true },
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
                modifier = Modifier.size(56.dp),
                painter = painterResource(R.drawable.ic_camera),
                contentDescription = null,
                tint = Colors.SECONDARY_TEXT
            )
        }
    }

    if (showActionsDialog) {
        AvatarActionsDialog(
            hasPhoto = hasPhoto,
            onDismissRequest = { showActionsDialog = false },
            onClickPick = {
                showActionsDialog = false
                ImagePickerHelper.launchPicker(launcher)
            },
            onClickRemove = {
                showActionsDialog = false
                onClear()
            }
        )
    }
}
