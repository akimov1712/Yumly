package ru.topbun.domain.useCases.config

import ru.topbun.domain.repository.config.ConfigRepository

class SetStatusFirstStartUseCase(private val repository: ConfigRepository) {

    suspend operator fun invoke(status: Boolean) = repository.setStatusFirstStart(status)

}