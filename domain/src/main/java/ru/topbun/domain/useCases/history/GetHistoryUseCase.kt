package ru.topbun.domain.useCases.history

import ru.topbun.domain.repository.history.HistoryRepository

class GetHistoryUseCase(private val repository: HistoryRepository) {

    suspend operator fun invoke() = repository.getHistory()

}