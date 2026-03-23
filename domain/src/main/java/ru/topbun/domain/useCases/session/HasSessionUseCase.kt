package ru.topbun.domain.useCases.session

import ru.topbun.domain.repository.session.SessionRepository

class HasSessionUseCase(
    private val repository: SessionRepository
) {

    operator fun invoke() = repository.hasSession()

}
