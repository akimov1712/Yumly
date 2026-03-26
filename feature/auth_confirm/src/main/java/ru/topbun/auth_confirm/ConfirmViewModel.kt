package ru.topbun.auth_confirm

import android.R.id.message
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.topbun.core.android.MVI
import ru.topbun.core.android.SnackbarManager
import ru.topbun.core.common.error.DataError
import ru.topbun.core.common.onError
import ru.topbun.core.common.onSuccess
import ru.topbun.domain.entity.verification.ConfirmVerificationEntity
import ru.topbun.domain.entity.verification.RequestVerificationEntity
import ru.topbun.domain.entity.verification.VerificationStatusType.CODE_EXPIRED
import ru.topbun.domain.entity.verification.VerificationStatusType.CODE_NO_MATCH
import ru.topbun.domain.entity.verification.VerificationType
import ru.topbun.domain.useCases.verification.ConfirmVerificationUseCase
import ru.topbun.domain.useCases.verification.RequestVerificationUseCase
import ru.topbun.navigation.auth.AuthConfirmMode
import ru.topbun.navigation.auth.AuthConfirmMode.RESET_PASSWORD
import ru.topbun.navigation.auth.AuthConfirmMode.SIGN_UP

class ConfirmViewModel(
    email: String,
    screenMode: AuthConfirmMode,
    private val snackbarManager: SnackbarManager,
    private val requestVerificationUseCase: RequestVerificationUseCase,
    private val confirmVerificationUseCase: ConfirmVerificationUseCase
) : MVI<ConfirmIntent, ConfirmState, ConfirmEvent>(ConfirmState(email, screenMode)) {

    private var timerJob: Job? = null

    private fun changeCode(code: String) {
        if (code.length <= 4) _state.update { it.copy(code = code) }
    }

    private suspend fun confirm(): Unit = with(state.value) {
        _state.update { it.copy(confirmLoading = true) }
        val type = when (screenMode) {
            SIGN_UP -> VerificationType.SIGN_UP_CONFIRM
            RESET_PASSWORD -> VerificationType.RESET_PASSWORD
        }
        val confirm = ConfirmVerificationEntity(email, code, type)
        val result = confirmVerificationUseCase(confirm)
        result.onSuccess {
            when (it) {
                CODE_EXPIRED -> snackbarManager.sendMessage("Код истек. Запросите новый")
                CODE_NO_MATCH -> snackbarManager.sendMessage("Коды не совпадают")
                else -> {
                    val event = when(screenMode){
                        SIGN_UP -> {
                            snackbarManager.sendMessage("Вы успешно подтвердили аккаунт")
                            ConfirmEvent.NavigateToDashboard
                        }
                        RESET_PASSWORD -> ConfirmEvent.NavigateToResetPassword(email)
                    }
                    _events.send(event)
                }
            }

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

        _state.update { it.copy(confirmLoading = false) }
    }

    private suspend fun sendAgain(): Unit = with(state.value) {
        _state.update { it.copy(sendAgainLoading = true) }
        val type = when (screenMode) {
            SIGN_UP -> VerificationType.SIGN_UP_CONFIRM
            RESET_PASSWORD -> VerificationType.RESET_PASSWORD
        }
        val verification = RequestVerificationEntity(email, type)
        val result = requestVerificationUseCase(verification)
        result.onSuccess {
            resetTimer()
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
        _state.update { it.copy(sendAgainLoading = false) }
    }

    override suspend fun handleIntent(intent: ConfirmIntent) {
        when (intent) {
            ConfirmIntent.ClickSendAgain -> sendAgain()
            is ConfirmIntent.ChangeCode -> changeCode(intent.code)
            ConfirmIntent.ClickConfirm -> confirm()
        }
    }

    fun startTimer() {
        timerJob?.cancel()

        timerJob = viewModelScope.launch {
            while (_state.value.timer > 0) {
                delay(1000)
                _state.update { current ->
                    current.copy(timer = current.timer - 1)
                }
            }
        }
    }

    fun resetTimer() {
        timerJob?.cancel()
        _state.update { it.copy(timer = 60) }
        startTimer()
    }

    init {
        resetTimer()
    }

}