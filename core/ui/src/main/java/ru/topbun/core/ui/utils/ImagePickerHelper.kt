package ru.topbun.core.ui.utils

import android.content.Context
import android.net.Uri
import android.os.Build
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
object ImagePickerHelper {

    private const val MAX_SIZE_BYTES = 8 * 1024 * 1024

    @Composable
    fun createImagePickerLauncher(
        context: Context,
        onImagePicked: (Uri) -> Unit,
        onError: (String) -> Unit
    ): ManagedActivityResultLauncher<Any, Uri?> {

        val launcher = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {

            rememberLauncherForActivityResult(
                contract = ActivityResultContracts.PickVisualMedia()
            ) { uri: Uri? ->
                handleResult(context, uri, onImagePicked, onError)
            }

        } else {

            rememberLauncherForActivityResult(
                contract = ActivityResultContracts.GetContent()
            ) { uri: Uri? ->
                handleResult(context, uri, onImagePicked, onError)
            }
        }

        @Suppress("UNCHECKED_CAST")
        return launcher as ManagedActivityResultLauncher<Any, Uri?>
    }

    fun launchPicker(launcher: ManagedActivityResultLauncher<Any, Uri?>) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            launcher.launch(
                PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
            )
        } else {
            launcher.launch("image/*")
        }
    }

    private fun handleResult(
        context: Context,
        uri: Uri?,
        onImagePicked: (Uri) -> Unit,
        onError: (String) -> Unit
    ) {
        if (uri == null) return

        val size = getFileSize(context, uri)

        if (size != null && size <= MAX_SIZE_BYTES) {
            onImagePicked(uri)
        } else {
            onError("Файл больше 8 МБ или не удалось определить размер")
        }
    }

    private fun getFileSize(context: Context, uri: Uri): Long? {
        return try {
            context.contentResolver.query(uri, null, null, null, null)?.use { cursor ->
                val index = cursor.getColumnIndex(android.provider.OpenableColumns.SIZE)
                if (cursor.moveToFirst() && index != -1) {
                    cursor.getLong(index)
                } else null
            }
        } catch (e: Exception) {
            null
        }
    }
}