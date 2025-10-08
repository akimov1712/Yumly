package ru.topbun.domain.useCases.gpt

import ru.topbun.domain.entity.gpt.SendMessageEntity
import ru.topbun.domain.repository.gpt.GptRepository

class SendGptMessageUseCase(private val repository: GptRepository) {

    suspend operator fun invoke(data: SendMessageEntity) = repository.sendMessage(data)

}