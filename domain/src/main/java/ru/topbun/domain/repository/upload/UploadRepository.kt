package ru.topbun.domain.repository.upload

import ru.topbun.core.common.Result
import ru.topbun.core.common.error.DataError

interface UploadRepository {

    suspend fun uploadFile(fileUri: String): Result<String, DataError>

}