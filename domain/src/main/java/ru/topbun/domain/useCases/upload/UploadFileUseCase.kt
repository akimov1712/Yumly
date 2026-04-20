package ru.topbun.domain.useCases.upload

import com.sun.jndi.toolkit.url.Uri
import ru.topbun.domain.repository.upload.UploadRepository
import java.net.URI

class UploadFileUseCase(private val repository: UploadRepository) {

    suspend operator fun invoke(filePath: Uri) = repository.uploadFile(filePath)

}