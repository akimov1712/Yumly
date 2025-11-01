package ru.topbun.domain.useCases.config

import ru.topbun.domain.repository.config.ConfigRepository

class GetStatusFirstStartUseCase(private val repository: ConfigRepository) {

    suspend operator fun invoke() = repository.getStatusFirstStart()

}