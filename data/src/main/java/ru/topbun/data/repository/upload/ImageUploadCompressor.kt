package ru.topbun.data.repository.upload

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.ByteArrayOutputStream

internal class ImageUploadCompressor(
    private val context: Context
) {

    suspend fun compress(uriString: String): CompressedImageData? = withContext(Dispatchers.IO) {
        val uri = Uri.parse(uriString)
        val bounds = BitmapFactory.Options().apply { inJustDecodeBounds = true }
        context.contentResolver.openInputStream(uri)?.use {
            BitmapFactory.decodeStream(it, null, bounds)
        } ?: return@withContext null

        val sampleSize = calculateInSampleSize(bounds.outWidth, bounds.outHeight)
        val bitmapOptions = BitmapFactory.Options().apply {
            inSampleSize = sampleSize
            inPreferredConfig = Bitmap.Config.ARGB_8888
        }

        val bitmap = context.contentResolver.openInputStream(uri)?.use {
            BitmapFactory.decodeStream(it, null, bitmapOptions)
        } ?: return@withContext null

        val scaledBitmap = scaleBitmapIfNeeded(bitmap)
        val hasAlpha = scaledBitmap.hasAlpha()
        val format = if (hasAlpha) Bitmap.CompressFormat.PNG else Bitmap.CompressFormat.JPEG
        val extension = if (hasAlpha) "png" else "jpg"
        val mimeType = if (hasAlpha) "image/png" else "image/jpeg"
        val quality = if (hasAlpha) 100 else 88

        val bytes = ByteArrayOutputStream().use { output ->
            scaledBitmap.compress(format, quality, output)
            output.toByteArray()
        }

        if (scaledBitmap !== bitmap) scaledBitmap.recycle()
        bitmap.recycle()

        CompressedImageData(
            bytes = bytes,
            fileName = "upload_${System.currentTimeMillis()}.$extension",
            mimeType = mimeType
        )
    }

    private fun scaleBitmapIfNeeded(bitmap: Bitmap): Bitmap {
        val maxSide = 1080
        if (bitmap.width <= maxSide && bitmap.height <= maxSide) return bitmap

        val scale = minOf(
            maxSide.toFloat() / bitmap.width.toFloat(),
            maxSide.toFloat() / bitmap.height.toFloat()
        )

        val scaledWidth = (bitmap.width * scale).toInt().coerceAtLeast(1)
        val scaledHeight = (bitmap.height * scale).toInt().coerceAtLeast(1)
        return Bitmap.createScaledBitmap(bitmap, scaledWidth, scaledHeight, true)
    }

    private fun calculateInSampleSize(width: Int, height: Int): Int {
        var inSampleSize = 1
        val maxSide = maxOf(width, height)

        while (maxSide / inSampleSize > 2160) {
            inSampleSize *= 2
        }
        return inSampleSize
    }
}

internal data class CompressedImageData(
    val bytes: ByteArray,
    val fileName: String,
    val mimeType: String,
)
