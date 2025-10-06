package ru.topbun.domain.useCases.account

import ru.topbun.domain.repository.account.AccountRepository

class UpdateAccountInfoUseCase(
    private val repository: AccountRepository
) {

    suspend operator fun invoke(username: String?, photoUrl: String?) =
        repository.updateAccountInfo(username, photoUrl)

}