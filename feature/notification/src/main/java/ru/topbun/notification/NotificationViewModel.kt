package ru.topbun.notification

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
import ru.topbun.domain.useCases.notification.GetNotificationsUseCase
import ru.topbun.domain.useCases.session.HasSessionUseCase

internal class NotificationViewModel(
    private val hasSessionUseCase: HasSessionUseCase,
    private val getNotificationsUseCase: GetNotificationsUseCase,
    private val snackbarManager: SnackbarManager,
) : MVI<NotificationIntent, NotificationState, NotificationEvent>(NotificationState()) {

    private var loadJob: Job? = null

    private fun checkSession() {
        val hasSession = hasSessionUseCase()
        val uiState = if (hasSession) {
            NotificationState.NotificationUiState.SUCCESS
        } else {
            NotificationState.NotificationUiState.NEED_AUTH
        }
        _state.update { it.copy(notificationUiState = uiState) }

        if (hasSession && state.value.list.status == ScreenUiState.Idle) {
            loadNotifications()
        }
    }

    private fun loadNotifications() {
        val list = state.value.list
        if (list.status.isLoading || list.isEndList) return

        loadJob?.cancel()
        loadJob = viewModelScope.launch(SupervisorJob()) {
            _state.update { it.copy(list = it.list.copy(status = ScreenUiState.Loading)) }

            getNotificationsUseCase(
                limit = PAGE_SIZE,
                offset = list.items.size
            ).onSuccess { items ->
                _state.update { current ->
                    val merged = (current.list.items + items).distinctBy { it.id }
                    current.copy(
                        list = current.list.copy(
                            items = merged,
                            status = ScreenUiState.Success,
                            isEndList = items.isEmpty()
                        )
                    )
                }
            }.onError { error, _ ->
                if (error == DataError.Network.UNAUTHORIZED) {
                    _state.update {
                        it.copy(
                            notificationUiState = NotificationState.NotificationUiState.NEED_AUTH,
                            list = NotificationState.NotificationListUiState()
                        )
                    }
                } else {
                    snackbarManager.showMessage(error.toMessage())
                    _state.update {
                        it.copy(list = it.list.copy(status = ScreenUiState.Error))
                    }
                }
            }
        }
    }

    private fun refreshNotifications() {
        loadJob?.cancel()
        _state.update { it.copy(list = NotificationState.NotificationListUiState()) }
        loadNotifications()
    }

    private fun clickInitiator(userId: Int) {
        viewModelScope.launch { _events.send(NotificationEvent.NavigateToProfile(userId)) }
    }

    private fun clickRecipe(recipeId: Int, authorUserId: Int) {
        viewModelScope.launch {
            _events.send(NotificationEvent.NavigateToRecipe(recipeId, authorUserId))
        }
    }

    override suspend fun handleIntent(intent: NotificationIntent) {
        when (intent) {
            NotificationIntent.CheckSession -> checkSession()
            NotificationIntent.LoadNotifications -> loadNotifications()
            NotificationIntent.RefreshNotifications -> refreshNotifications()
            is NotificationIntent.ClickInitiator -> clickInitiator(intent.userId)
            is NotificationIntent.ClickRecipe -> clickRecipe(intent.recipeId, intent.authorUserId)
        }
    }

    private fun DataError.toMessage(): String = when (this) {
        DataError.Network.UNAUTHORIZED -> "Пользователь не авторизован"
        DataError.Network.BAD_REQUEST,
        DataError.Network.INVALID_DATA -> "Проверьте корректность введённых данных"
        DataError.Network.REQUEST_TIMEOUT -> "Время ожидания превышено. Проверьте интернет-соединение или попробуйте позже"
        DataError.Network.SERIALIZATION -> "При получении данных произошла ошибка"
        DataError.Network.SERVER_ERROR -> "Произошла ошибка на сервере. Попробуйте позже"
        DataError.Network.NO_INTERNET -> "Отсутствует интернет-соединение"
        else -> "Произошла ошибка. Попробуйте позже"
    }

    companion object {
        private const val PAGE_SIZE = 20
    }
}
