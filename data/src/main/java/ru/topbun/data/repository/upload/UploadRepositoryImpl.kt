package ru.topbun.data.repository.upload

import android.content.Context
import android.net.Uri
import android.provider.OpenableColumns
import androidx.core.net.toUri
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import ru.topbun.core.common.HttpStatusCode
import ru.topbun.core.common.Result
import ru.topbun.core.common.error.DataError
import ru.topbun.data.exceptionWrapper
import ru.topbun.data.source.remote.api.upload.UploadApi
import ru.topbun.domain.repository.upload.UploadRepository

internal class UploadRepositoryImpl(
    private val context: Context,
    private val api: UploadApi
) : UploadRepository {

    override suspend fun uploadFile(fileUri: String): Result<String, DataError> =
        context.exceptionWrapper {
            val multipart = createMultipartBodyPart(fileUri)
                ?: return@exceptionWrapper Result.Error(DataError.Network.INVALID_DATA)

            val response = api.uploadImage(multipart)
            val body = response.body()

            if (response.isSuccessful && body != null) {
                Result.Success(body.url)
            } else {
                val error = when (response.code()) {
                    HttpStatusCode.BAD_REQUEST -> DataError.Network.INVALID_DATA
                    HttpStatusCode.UNAUTHORIZED -> DataError.Network.UNAUTHORIZED
                    else -> DataError.Network.SERVER_ERROR
                }
                Result.Error(error)
            }
        }

    private fun createMultipartBodyPart(fileUri: String): MultipartBody.Part? {
        val uri = fileUri.toUri()
        val bytes = context.contentResolver.openInputStream(uri)?.use { it.readBytes() } ?: return null
        val mimeType = context.contentResolver.getType(uri) ?: DEFAULT_MIME_TYPE
        val fileName = getFileName(uri, mimeType)

        val requestBody = bytes.toRequestBody(mimeType.toMediaTypeOrNull())
        return MultipartBody.Part.createFormData(FILE_PART_NAME, fileName, requestBody)
    }

    private fun getFileName(uri: Uri, mimeType: String): String {
        context.contentResolver.query(uri, null, null, null, null)?.use { cursor ->
            val displayNameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
            if (displayNameIndex != -1 && cursor.moveToFirst()) {
                val displayName = cursor.getString(displayNameIndex)
                if (!displayName.isNullOrBlank()) return displayName
            }
        }

        val extension = mimeType.substringAfter('/', DEFAULT_EXTENSION)
        return "upload_image.$extension"
    }

    private companion object {
        const val FILE_PART_NAME = "file"
        const val DEFAULT_MIME_TYPE = "image/jpeg"
        const val DEFAULT_EXTENSION = "jpg"
    }
}
