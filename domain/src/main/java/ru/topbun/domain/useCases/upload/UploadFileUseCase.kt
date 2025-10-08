package ru.topbun.domain.useCases.upload

import ru.topbun.domain.repository.upload.UploadRepository
import java.net.URI

class UploadFileUseCase(private val repository: UploadRepository) {

    suspend operator fun invoke(filePath: URI) = repository.uploadFile(filePath)

}