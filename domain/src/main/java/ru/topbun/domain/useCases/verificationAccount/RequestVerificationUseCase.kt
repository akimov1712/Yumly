package ru.topbun.domain.useCases.verificationAccount

import ru.topbun.domain.entity.verificationAccount.RequestVerificationEntity
import ru.topbun.domain.repository.verificationAccount.VerificationRepository

class RequestVerificationUseCase(private val repository: VerificationRepository) {

    suspend operator fun invoke(verification: RequestVerificationEntity) =
        repository.request(verification)

}