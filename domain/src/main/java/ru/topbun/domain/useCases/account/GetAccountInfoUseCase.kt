package ru.topbun.domain.useCases.account

import ru.topbun.domain.repository.account.AccountRepository

class GetAccountInfoUseCase(
    private val repository: AccountRepository
) {

    suspend operator fun invoke() = repository.getAccountInfo()

}