package ru.topbun.domain.useCases.verification

import ru.topbun.domain.entity.verification.RequestVerificationEntity
import ru.topbun.domain.repository.verification.VerificationRepository

class RequestVerificationUseCase(private val repository: VerificationRepository) {

    suspend operator fun invoke(verification: RequestVerificationEntity) =
        repository.request(verification)

}