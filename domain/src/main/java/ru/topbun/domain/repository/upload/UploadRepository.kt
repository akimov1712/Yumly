package ru.topbun.domain.repository.upload

import ru.topbun.core.common.error.DataError
import ru.topbun.core.common.Result
import java.net.URI

interface UploadRepository {

    suspend fun uploadFile(filePath: URI): Result<String, DataError>

}