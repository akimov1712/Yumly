package ru.topbun.domain.useCases.account

import ru.topbun.domain.repository.account.AccountRepository

class GetProfileUseCase(
    private val repository: AccountRepository
) {

    suspend operator fun invoke(userId: Int) = repository.getProfile(userId)

}