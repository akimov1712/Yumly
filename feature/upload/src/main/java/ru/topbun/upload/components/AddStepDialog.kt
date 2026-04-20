package ru.topbun.upload.components

import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.max
import androidx.compose.ui.unit.sp
import coil3.compose.rememberAsyncImagePainter
import org.koin.compose.koinInject
import ru.topbun.core.android.SnackbarManager
import ru.topbun.core.ui.R
import ru.topbun.core.ui.components.AppButton
import ru.topbun.core.ui.components.AppOutlinedButton
import ru.topbun.core.ui.components.AppOutlinedTextField
import ru.topbun.core.ui.components.BottomDialogWrapper
import ru.topbun.core.ui.components.Height
import ru.topbun.core.ui.theme.Colors
import ru.topbun.core.ui.theme.Typography
import ru.topbun.core.ui.utils.ImagePickerHelper
import ru.topbun.core.ui.utils.rippleClickable

@Composable
internal fun AddStepDialog(
    onDismissRequest: () -> Unit,
    onClickConfirm: (description: String, previewUri: String?) -> Unit,
) {
    var description by rememberSaveable { mutableStateOf("") }
    var previewUri by rememberSaveable { mutableStateOf<String?>(null) }
    val buttonEnabled = description.isNotBlank()

    BottomDialogWrapper(
        onDismissRequest = onDismissRequest,
        containerColor = Colors.WHITE
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp, vertical = 16.dp)
        ) {
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = "Добавить шаг",
                style = Typography.H2,
                color = Colors.MAIN_TEXT,
                textAlign = TextAlign.Center
            )
            Height(10.dp)
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = "Добавьте описание шага и при необходимости изображение",
                style = Typography.P2,
                lineHeight = 17.sp,
                color = Colors.SECONDARY_TEXT,
                textAlign = TextAlign.Center
            )
            Height(20.dp)
            AppOutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = 140.dp, max = 180.dp),
                text = description,
                placeholder = "Например, смешайте все ингредиенты до однородности",
                supportText = "${description.length}/500",
                singleLine = false,
                shape = RoundedCornerShape(24.dp),
                onValueChange = {
                    if (it.length <= 500) description = it
                }
            )
            Height(12.dp)
            StepImagePicker(
                previewUri = previewUri?.let(Uri::parse),
                onChangePreview = { previewUri = it?.toString() }
            )
            Height(20.dp)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                AppOutlinedButton(
                    text = "Отмена",
                    modifier = Modifier
                        .weight(1f)
                        .defaultMinSize(minHeight = 48.dp),
                    onClick = onDismissRequest,
                    borderColor = Colors.OUTLINE,
                    contentColor = Colors.BLUE_TEXT,
                )
                AppButton(
                    text = "Добавить",
                    modifier = Modifier
                        .weight(1f)
                        .defaultMinSize(minHeight = 48.dp),
                    enabled = buttonEnabled,
                    onClick = { onClickConfirm(description, previewUri) }
                )
            }
        }
}
}

@Composable
private fun StepImagePicker(
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

    if (previewUri == null) {
        Box(
            modifier = Modifier.fillMaxWidth()
                .height(48.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(Colors.FORM)
                .rippleClickable(Colors.BLACK){ ImagePickerHelper.launchPicker(launcher) }
                .padding(10.dp),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                modifier = Modifier.fillMaxHeight(),
                painter = painterResource(R.drawable.ic_camera),
                contentDescription = null,
                tint = Colors.BLUE_TEXT
            )
        }
    } else {
        StepPreviewImage(previewUri) {
            onChangePreview(null)
        }
    }
}

@Composable
private fun StepPreviewImage(
    previewUri: Uri,
    onClearPreview: () -> Unit
) {
    Box{
        Image(
            modifier = Modifier.fillMaxWidth()
                .heightIn(max = 400.dp)
                .clip(RoundedCornerShape(16.dp)),
            painter = rememberAsyncImagePainter(previewUri),
            contentDescription = null,
            contentScale = ContentScale.FillWidth
        )
        IconButton(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(12.dp)
                .clip(CircleShape)
                .background(Colors.BLACK.copy(alpha = 0.35f)),
            onClick = onClearPreview
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
