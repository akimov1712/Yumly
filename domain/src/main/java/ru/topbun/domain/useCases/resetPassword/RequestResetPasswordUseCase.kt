package ru.topbun.domain.useCases.resetPassword

import ru.topbun.domain.entity.resetPassword.RequestResetPasswordEntity
import ru.topbun.domain.entity.verificationAccount.RequestVerificationEntity
import ru.topbun.domain.repository.resetPassword.ResetPasswordRepository
import ru.topbun.domain.repository.verificationAccount.VerificationRepository

class RequestResetPasswordUseCase(private val repository: ResetPasswordRepository) {

    suspend operator fun invoke(verification: RequestResetPasswordEntity) =
        repository.request(verification)

}