package ru.topbun.domain.useCases.login

import ru.topbun.domain.entity.login.LoginEntity
import ru.topbun.domain.repository.login.LoginRepository

class LoginUseCase(private val repository: LoginRepository) {

    suspend operator fun invoke(login: LoginEntity) = repository.login(login)

}