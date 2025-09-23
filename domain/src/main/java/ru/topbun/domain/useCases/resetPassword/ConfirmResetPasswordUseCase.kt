package ru.topbun.domain.useCases.resetPassword

import ru.topbun.domain.entity.resetPassword.ConfirmResetPasswordEntity
import ru.topbun.domain.entity.verificationAccount.ConfirmVerificationEntity
import ru.topbun.domain.repository.resetPassword.ResetPasswordRepository
import ru.topbun.domain.repository.verificationAccount.VerificationRepository

class ConfirmResetPasswordUseCase(private val repository: ResetPasswordRepository) {

    suspend operator fun invoke(confirm: ConfirmResetPasswordEntity) = repository.confirm(confirm)

}