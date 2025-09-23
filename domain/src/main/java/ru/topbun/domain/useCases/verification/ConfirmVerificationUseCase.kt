package ru.topbun.domain.useCases.verification

import ru.topbun.domain.entity.verification.ConfirmVerificationEntity
import ru.topbun.domain.repository.verification.VerificationRepository

class ConfirmVerificationUseCase(private val repository: VerificationRepository) {

    suspend operator fun invoke(confirm: ConfirmVerificationEntity) = repository.confirm(confirm)

}