package ru.topbun.domain.useCases.account

import ru.topbun.domain.entity.account.UpdateAccountInfoEntity
import ru.topbun.domain.repository.account.AccountRepository

class UpdateAccountInfoUseCase(
    private val repository: AccountRepository
) {

    suspend operator fun invoke(data: UpdateAccountInfoEntity) =
        repository.updateAccountInfo(data)

}