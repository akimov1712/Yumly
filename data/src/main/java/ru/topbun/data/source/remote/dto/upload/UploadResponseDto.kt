package ru.topbun.data.source.remote.dto.upload

import com.google.gson.annotations.SerializedName

internal data class UploadResponseDto(
    @SerializedName("path") val path: String? = null,
    @SerializedName("url") val url: String? = null,
    @SerializedName("filePath") val filePath: String? = null,
    @SerializedName("fileUrl") val fileUrl: String? = null,
)

internal fun UploadResponseDto.toPath(): String? = path ?: url ?: filePath ?: fileUrl
