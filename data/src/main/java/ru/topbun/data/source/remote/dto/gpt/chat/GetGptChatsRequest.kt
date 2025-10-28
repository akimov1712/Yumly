package ru.topbun.data.source.remote.dto.gpt.chat

internal data class GetGptChatsRequest(
    val offset: Int,
    val limit: Int,
)
