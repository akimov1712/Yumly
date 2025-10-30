package ru.topbun.data.source.remote.api.upload

import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import ru.topbun.data.source.remote.dto.upload.UploadResponse

internal interface UploadApi {

    @Multipart
    @POST("/v1/upload")
    fun uploadImage(@Part filePart: MultipartBody.Part): Response<UploadResponse>

}