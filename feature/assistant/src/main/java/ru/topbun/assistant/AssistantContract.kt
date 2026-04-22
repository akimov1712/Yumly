package ru.topbun.assistant

import androidx.compose.foundation.lazy.LazyListState
import ru.topbun.domain.ScreenUiState
import ru.topbun.domain.entity.gpt.GptChatEntity
import ru.topbun.domain.entity.gpt.GptMessageEntity

internal data class AssistantState(
    val chatList: ChatListUiState = ChatListUiState(),
    val chatListState: LazyListState = LazyListState(),
    val selectedChat: GptChatEntity? = null,
    val messageText: String = "",
    val assistantUiState: AssistantUiState? = null,
    val sendMessageStatus: ScreenUiState = ScreenUiState.Idle,
    val showHistoryDialog: Boolean = false,
    val optimisticMessages: List<GptMessageEntity> = emptyList()
) {

    val isMessageLimitReached: Boolean
        get() = selectedChat?.let { it.messages.size >= it.maxLimitMessages } ?: false

    val canSendMessage: Boolean
        get() = messageText.isNotBlank() && !sendMessageStatus.isLoading && !isMessageLimitReached

    val visibleMessages: List<GptMessageEntity>
        get() = selectedChat?.messages.orEmpty() + optimisticMessages

    data class ChatListUiState(
        val chats: List<GptChatEntity> = emptyList(),
        val status: ScreenUiState = ScreenUiState.Idle,
        val isEndList: Boolean = false,
    )

    enum class AssistantUiState {
        SUCCESS, NEED_AUTH
    }
}

internal sealed interface AssistantIntent {

    data object CheckSession: AssistantIntent
    data object LoadChats: AssistantIntent
    data object RefreshChats: AssistantIntent
    data object StartNewChat: AssistantIntent
    data object SendMessage: AssistantIntent
    data class SelectChat(val chat: GptChatEntity): AssistantIntent
    data class ChangeMessageText(val value: String): AssistantIntent
    data class ChangeShowHistoryDialog(val value: Boolean): AssistantIntent

}

internal sealed interface AssistantEvent
