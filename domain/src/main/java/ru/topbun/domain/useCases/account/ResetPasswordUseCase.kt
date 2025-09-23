package ru.topbun.domain.useCases.account

import ru.topbun.domain.entity.account.ResetPasswordEntity
import ru.topbun.domain.repository.account.AccountRepository

class ResetPasswordUseCase(private val repository: AccountRepository) {

    suspend operator fun invoke(resetPassword: ResetPasswordEntity) =
        repository.resetPassword(resetPassword)

}