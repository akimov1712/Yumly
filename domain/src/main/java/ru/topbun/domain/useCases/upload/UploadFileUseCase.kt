package ru.topbun.domain.useCases.upload

import ru.topbun.domain.repository.upload.UploadRepository

class UploadFileUseCase(private val repository: UploadRepository) {

    suspend operator fun invoke(fileUri: String) = repository.uploadFile(fileUri)

}