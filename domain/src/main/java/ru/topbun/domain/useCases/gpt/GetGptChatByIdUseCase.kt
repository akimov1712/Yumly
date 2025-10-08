package ru.topbun.domain.useCases.gpt

import ru.topbun.domain.repository.gpt.GptRepository

class GetGptChatByIdUseCase(private val repository: GptRepository) {

    suspend operator fun invoke(id: Int) = repository.getChatById(id)

}