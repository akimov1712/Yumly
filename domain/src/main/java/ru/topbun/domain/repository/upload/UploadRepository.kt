package ru.topbun.domain.repository.upload

import com.sun.jndi.toolkit.url.Uri
import ru.topbun.core.common.error.DataError
import ru.topbun.core.common.Result
import java.net.URI

interface UploadRepository {

    suspend fun uploadFile(filePath: Uri): Result<String, DataError>

}