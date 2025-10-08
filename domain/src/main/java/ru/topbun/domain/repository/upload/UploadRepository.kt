package ru.topbun.domain.repository.upload

import ru.topbun.common.DataError
import ru.topbun.common.Result
import java.net.URI

interface UploadRepository {

    suspend fun uploadFile(filePath: URI): Result<String, DataError>

}