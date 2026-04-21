package ru.topbun.assistant

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.topbun.core.android.MVI
import ru.topbun.core.android.SnackbarManager
import ru.topbun.core.common.error.DataError
import ru.topbun.core.common.onError
import ru.topbun.core.common.onSuccess
import ru.topbun.domain.ScreenUiState
import ru.topbun.domain.entity.gpt.GptChatEntity
import ru.topbun.domain.entity.gpt.SendMessageEntity
import ru.topbun.domain.useCases.gpt.GetGptChatsUseCase
import ru.topbun.domain.useCases.gpt.SendGptMessageUseCase
import ru.topbun.domain.useCases.session.HasSessionUseCase

internal class AssistantViewModel(
    private val hasSessionUseCase: HasSessionUseCase,
    private val getGptChatsUseCase: GetGptChatsUseCase,
    private val sendGptMessageUseCase: SendGptMessageUseCase,
    private val snackbarManager: SnackbarManager,
): MVI<AssistantIntent, AssistantState, AssistantEvent>(AssistantState()) {

    private var chatLoadJob: Job? = null
    private var sendMessageJob: Job? = null

    private fun checkSession() {
        val hasSession = hasSessionUseCase()
        val uiState = if (hasSession) {
            AssistantState.AssistantUiState.SUCCESS
        } else {
            AssistantState.AssistantUiState.NEED_AUTH
        }
        _state.update { it.copy(assistantUiState = uiState) }

        if (hasSession && state.value.chatList.status == ScreenUiState.Idle) {
            loadChats()
        }
    }

    private fun loadChats() {
        val chatList = state.value.chatList
        if (chatList.status.isLoading || chatList.isEndList) return

        chatLoadJob?.cancel()
        chatLoadJob = viewModelScope.launch(SupervisorJob()) {
            _state.update {
                it.copy(chatList = it.chatList.copy(status = ScreenUiState.Loading))
            }

            getGptChatsUseCase(
                limit = CHAT_PAGE_SIZE,
                offset = chatList.chats.size
            ).onSuccess { chats ->
                _state.update { current ->
                    val mergedChats = (current.chatList.chats + chats)
                        .distinctBy { it.id }
                        .sortedByDescending { it.createdAt }
                    val selectedChat = current.selectedChat?.let { selected ->
                        mergedChats.firstOrNull { it.id == selected.id } ?: selected
                    }

                    current.copy(
                        selectedChat = selectedChat,
                        chatList = current.chatList.copy(
                            chats = mergedChats,
                            status = ScreenUiState.Success,
                            isEndList = chats.isEmpty()
                        )
                    )
                }
            }.onError { error, _ ->
                snackbarManager.showMessage(error.toMessage())
                _state.update {
                    it.copy(chatList = it.chatList.copy(status = ScreenUiState.Error))
                }
            }
        }
    }

    private fun refreshChats() {
        chatLoadJob?.cancel()
        _state.update { it.copy(chatList = AssistantState.ChatListUiState()) }
        loadChats()
    }

    private fun selectChat(chat: GptChatEntity) {
        _state.update {
            it.copy(
                selectedChat = chat,
                messageText = "",
                sendMessageStatus = ScreenUiState.Idle
            )
        }
    }

    private fun startNewChat() {
        _state.update {
            it.copy(
                selectedChat = null,
                messageText = "",
                sendMessageStatus = ScreenUiState.Idle
            )
        }
    }

    private fun changeMessageText(value: String) {
        if (value.length <= MAX_MESSAGE_LENGTH) {
            _state.update { it.copy(messageText = value) }
        }
    }

    private fun sendMessage() {
        val currentState = state.value
        val text = currentState.messageText.trim()
        if (text.isBlank() || currentState.sendMessageStatus.isLoading || currentState.isMessageLimitReached) return

        sendMessageJob?.cancel()
        sendMessageJob = viewModelScope.launch(SupervisorJob()) {
            _state.update { it.copy(sendMessageStatus = ScreenUiState.Loading) }

            sendGptMessageUseCase(
                SendMessageEntity(
                    chatId = currentState.selectedChat?.id,
                    text = text
                )
            ).onSuccess { chat ->
                _state.update { current ->
                    val chats = (listOf(chat) + current.chatList.chats.filterNot { it.id == chat.id })
                        .sortedByDescending { it.createdAt }

                    current.copy(
                        selectedChat = chat,
                        messageText = "",
                        sendMessageStatus = ScreenUiState.Success,
                        chatList = current.chatList.copy(
                            chats = chats,
                            status = ScreenUiState.Success
                        )
                    )
                }
            }.onError { error, _ ->
                snackbarManager.showMessage(error.toMessage())
                _state.update { it.copy(sendMessageStatus = ScreenUiState.Error) }
            }
        }
    }

    override suspend fun handleIntent(intent: AssistantIntent) {
        when(intent) {
            AssistantIntent.CheckSession -> checkSession()
            AssistantIntent.LoadChats -> loadChats()
            AssistantIntent.RefreshChats -> refreshChats()
            AssistantIntent.StartNewChat -> startNewChat()
            AssistantIntent.SendMessage -> sendMessage()
            is AssistantIntent.SelectChat -> selectChat(intent.chat)
            is AssistantIntent.ChangeMessageText -> changeMessageText(intent.value)
        }
    }

    private fun DataError.toMessage(): String = when(this) {
        DataError.Network.UNAUTHORIZED -> "Пользователь не авторизован"
        DataError.Network.BAD_REQUEST, DataError.Network.INVALID_DATA -> "Проверьте корректность введённых данных"
        DataError.Network.NOT_FOUND -> "Чат не найден"
        DataError.Network.FORBIDDEN -> "Нет доступа к этому чату"
        DataError.Network.REQUEST_TIMEOUT -> "Время ожидания превышено. Проверьте интернет-соединение или попробуйте позже"
        DataError.Network.SERIALIZATION -> "При получении данных произошла ошибка"
        DataError.Network.SERVER_ERROR -> "Произошла ошибка на сервере. Попробуйте позже"
        DataError.Network.NO_INTERNET -> "Отсутствует интернет-соединение"
        else -> "Произошла ошибка. Попробуйте позже"
    }

    companion object {
        private const val CHAT_PAGE_SIZE = 20
        private const val MAX_MESSAGE_LENGTH = 1000
    }
}
