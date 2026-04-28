package ru.topbun.domain.useCases.session

import ru.topbun.domain.repository.session.SessionRepository

class LogoutUseCase(
    private val repository: SessionRepository
) {

    suspend operator fun invoke() = repository.clearSession()

}
