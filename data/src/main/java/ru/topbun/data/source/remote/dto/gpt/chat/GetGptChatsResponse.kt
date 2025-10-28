package ru.topbun.data.source.remote.dto.gpt.chat

internal class GetGptChatsResponse: ArrayList<GptChatResponse>(){

    internal fun toEntity() = this.map { it.toEntity() }

}

