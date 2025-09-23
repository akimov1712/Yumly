package ru.topbun.domain.useCases.verificationAccount

import ru.topbun.domain.entity.verificationAccount.ConfirmVerificationEntity
import ru.topbun.domain.repository.verificationAccount.VerificationRepository

class ConfirmVerificationUseCase(private val repository: VerificationRepository) {

    suspend operator fun invoke(confirm: ConfirmVerificationEntity) = repository.confirm(confirm)

}