package ru.topbun.auth_reset_request

import kotlinx.coroutines.flow.update
import ru.topbun.core.android.MVI
import ru.topbun.core.android.SnackbarManager
import ru.topbun.core.common.error.DataError
import ru.topbun.core.common.onError
import ru.topbun.core.common.onSuccess
import ru.topbun.domain.entity.verification.RequestVerificationEntity
import ru.topbun.domain.entity.verification.VerificationType
import ru.topbun.domain.useCases.verification.RequestVerificationUseCase

internal class ResetRequestViewModel(
    private val snackbarManager: SnackbarManager,
    private val requestVerificationUseCase: RequestVerificationUseCase
): MVI<ResetRequestIntent, ResetRequestState, ResetRequestEvent>(ResetRequestState()) {

    private fun changeEmail(value: String) { if (value.length <= 64) _state.update { it.copy(email = value) }}

    private suspend fun requestReset(){
        _state.update { it.copy(isLoading = true) }
        val verification = RequestVerificationEntity(state.value.email, VerificationType.RESET_PASSWORD)
        val result = requestVerificationUseCase(verification)
        result.onSuccess {
            _events.send(ResetRequestEvent.NavigateToConfirm(state.value.email, VerificationType.RESET_PASSWORD))
        }.onError { error, _ ->
            val message = when (error) {
                DataError.Network.NOT_FOUND -> "Пользователь с указанной почтой не найден"
                DataError.Network.INVALID_DATA -> "Пользователь с указанной почтой или паролем не найден"
                DataError.Network.EXISTS -> "Пользователь с указанной почтой или паролем уже зарегистрирован"
                DataError.Network.REQUEST_TIMEOUT -> "Время ожидание превышено. Проверьте интернет соединение или попробуйте позже"
                DataError.Network.SERIALIZATION -> "При получении данных произошла ошибка"
                DataError.Network.SERVER_ERROR -> "Произошла ошибка на сервере. Попробуйте позже"
                DataError.Network.NO_INTERNET -> "Отсутствует интернет соединение"
                else -> "Произошла ошибка. Попробуйте позже"
            }
            snackbarManager.sendMessage(message)
        }
        _state.update { it.copy(isLoading = false) }
    }

    override suspend fun handleIntent(intent: ResetRequestIntent) {
        when(intent){
            is ResetRequestIntent.ChangeEmail -> changeEmail(intent.value)
            ResetRequestIntent.ClickReset -> requestReset()
        }
    }


}