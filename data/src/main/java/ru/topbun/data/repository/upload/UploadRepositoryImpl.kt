package ru.topbun.data.repository.upload

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import ru.topbun.common.HttpStatusCode
import ru.topbun.common.Result
import ru.topbun.common.error.DataError
import ru.topbun.data.BuildConfig
import ru.topbun.data.exceptionWrapper
import ru.topbun.data.source.remote.api.upload.UploadApi
import ru.topbun.domain.repository.upload.UploadRepository
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.net.URI

internal class UploadRepositoryImpl(
    private val context: Context,
    private val api: UploadApi
): UploadRepository {

    override suspend fun uploadFile(fileUri: URI): Result<String, DataError> =
        context.exceptionWrapper {
            val filePath = fileUri.path ?: return@exceptionWrapper Result.Error(DataError.Local.FILE_NOT_FOUND)
            val file = File(filePath)

            val uploadFile = if (file.length() > MAX_SIZE_BYTES) {
                compressImage(file)
            } else file

            if (uploadFile == null) return@exceptionWrapper Result.Error(DataError.Local.FILE_COMPRESS)

            val requestFile = uploadFile
                .asRequestBody("image/*".toMediaTypeOrNull())

            val body = MultipartBody.Part.createFormData(
                "file", uploadFile.name, requestFile
            )

            val response = api.uploadImage(body)
            val url = response.body()?.url

            if (uploadFile != file && uploadFile.exists()) {
                uploadFile.delete()
            }

            if (response.isSuccessful && url != null) {
                Result.Success(url.resolveLink())
            } else {
                val error = when(response.code()){
                    HttpStatusCode.BAD_REQUEST -> DataError.Network.BAD_REQUEST
                    else -> DataError.Network.SERVER_ERROR
                }
                Result.Error(error)
            }




        }

    private suspend fun compressImage(file: File): File? = withContext(Dispatchers.IO){
        val options = BitmapFactory.Options().apply {
            inJustDecodeBounds = true
        }
        BitmapFactory.decodeFile(file.absolutePath, options)

        options.inSampleSize = calculateInSampleSize(options, 1920, 1080)
        options.inJustDecodeBounds = false
        val bitmap = BitmapFactory.decodeFile(file.absolutePath, options)
            ?: return@withContext null

        val compressedFile = File(file.parent, "compressed_${file.nameWithoutExtension}.jpg")

        var quality = 100
        var byteArray: ByteArray

        do {
            val baos = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.JPEG, quality, baos)
            byteArray = baos.toByteArray()
            baos.close()
            quality -= 5
        } while (byteArray.size > MAX_SIZE_BYTES && quality > 10)

        FileOutputStream(compressedFile).use {
            it.write(byteArray)
        }

        bitmap.recycle()
        return@withContext compressedFile
    }

    private fun calculateInSampleSize(options: BitmapFactory.Options, reqWidth: Int, reqHeight: Int): Int {
        val (height: Int, width: Int) = options.run { outHeight to outWidth }
        var inSampleSize = 1

        if (height > reqHeight || width > reqWidth) {
            val halfHeight: Int = height / 2
            val halfWidth: Int = width / 2
            while ((halfHeight / inSampleSize) >= reqHeight && (halfWidth / inSampleSize) >= reqWidth) {
                inSampleSize *= 2
            }
        }
        return inSampleSize
    }


    private fun String.resolveLink() = "${BuildConfig.BASE_URL}/v1$this"

    companion object {
        private const val MAX_SIZE_BYTES = 8 * 1024 * 1024

    }

}