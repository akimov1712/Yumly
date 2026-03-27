package ru.topbun.auth_reset

import android.R.id.message
import kotlinx.coroutines.flow.update
import ru.topbun.core.android.MVI
import ru.topbun.core.android.SnackbarManager
import ru.topbun.core.common.error.DataError
import ru.topbun.core.common.onError
import ru.topbun.core.common.onSuccess
import ru.topbun.domain.entity.account.ResetPasswordEntity
import ru.topbun.domain.useCases.account.ResetPasswordUseCase
import ru.topbun.domain.validation.account.ResetPasswordValidator
import ru.topbun.domain.validation.account.ResetPasswordValidatorError.*

class ResetViewModel(
    email: String,
    private val snackbarManager: SnackbarManager,
    private val resetPasswordUseCase: ResetPasswordUseCase,
    private val resetPasswordValidator: ResetPasswordValidator,
): MVI<ResetIntent, ResetState, ResetEvent>(ResetState(email)){

    private fun changePassword(value: String){ if (value.length <= 64) _state.update { it.copy(password = value) } }
    private fun changeConfirmPassword(value: String){ if (value.length <= 64) _state.update { it.copy(confirmPassword = value) } }
    private fun switchShowPassword(){ _state.update { it.copy(showPassword = !_state.value.showPassword) } }

    private suspend fun resetPassword(): Unit = with(state.value){
        val resetPassword = ResetPasswordEntity(email, password, confirmPassword)
        val isValid = resetPasswordValidator.validate(resetPassword)
        isValid.onSuccess {
            _state.update { it.copy(resetLoading = true) }
            val result = resetPasswordUseCase(resetPassword)
            result.onSuccess {
                _events.send(ResetEvent.NavigateToLogin)
                snackbarManager.showMessage("Вы успешно изменили пароль")
            }.onError { error, _ ->
                val message = when (error) {
                    DataError.Network.NOT_FOUND -> "Пользователь не найден"
                    DataError.Network.REQUEST_TIMEOUT -> "Время ожидание превышено. Проверьте интернет соединение или попробуйте позже"
                    DataError.Network.SERIALIZATION -> "При получении данных произошла ошибка"
                    DataError.Network.SERVER_ERROR -> "Произошла ошибка на сервере. Попробуйте позже"
                    DataError.Network.NO_INTERNET -> "Отсутствует интернет соединение"
                    else -> "Произошла ошибка. Попробуйте позже"
                }
                snackbarManager.showMessage(message)
            }
            _state.update { it.copy(resetLoading = false) }
        }.onError { error, _ ->
            val message = when(error){
                PASSWORD_EMPTY -> "Пароль не может быть пустым"
                PASSWORD_SHORT -> "Минимальная длина пароля 6 символов"
                PASSWORD_NOT_MATCH -> "Пароли не совпадают"
            }
            snackbarManager.showMessage(message)
        }
    }

    override suspend fun handleIntent(intent: ResetIntent) {
        when(intent){
            is ResetIntent.ChangePassword -> changePassword(intent.value)
            is ResetIntent.ChangeConfirmPassword -> changeConfirmPassword(intent.value)
            ResetIntent.SwitchShowPassword -> switchShowPassword()
            ResetIntent.ClickReset -> resetPassword()
        }
    }

}