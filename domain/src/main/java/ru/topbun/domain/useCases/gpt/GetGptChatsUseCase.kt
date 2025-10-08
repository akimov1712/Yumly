package ru.topbun.domain.useCases.gpt

import ru.topbun.domain.repository.gpt.GptRepository

class GetGptChatsUseCase(private val repository: GptRepository) {

    suspend operator fun invoke(limit: Int = 20, offset: Int = 0) = repository.getChats(limit, offset)

}