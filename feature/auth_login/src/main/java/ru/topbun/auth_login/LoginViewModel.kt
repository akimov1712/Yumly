package ru.topbun.auth_login

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.update
import ru.topbun.core.android.MVI
import ru.topbun.core.android.SnackbarManager
import ru.topbun.core.common.error.DataError
import ru.topbun.core.common.onError
import ru.topbun.core.common.onSuccess
import ru.topbun.domain.entity.login.LoginEntity
import ru.topbun.domain.useCases.login.LoginUseCase
import ru.topbun.domain.validation.login.LoginValidator
import ru.topbun.domain.validation.login.LoginValidatorError
import ru.topbun.domain.validation.login.LoginValidatorError.*

class LoginViewModel(
    private val loginValidator: LoginValidator,
    private val snackbarManager: SnackbarManager,
    private val loginUseCase: LoginUseCase
): MVI<LoginIntent, LoginState, LoginEvent>(LoginState()) {

    private fun changeEmail(value: String) = _state.update { it.copy(email = value) }
    private fun changePassword(value: String) = _state.update { it.copy(password = value) }
    private fun switchShowPassword() = _state.update { it.copy(showPassword = !_state.value.showPassword) }

    private suspend fun login(): Unit = with(state.value){
        val login = LoginEntity(email, password)
        val validResult = loginValidator.validate(login)
        validResult.onSuccess { _ ->
            _state.update { it.copy(loginIsLoading = true) }
            val result = loginUseCase(login)
            result.onSuccess {
                snackbarManager.sendMessage("Пользователь авторизован")
            }.onError { error, _ ->
                if (error == DataError.Network.NOT_VERIFIED){
                    snackbarManager.sendMessage("Подтвердите почту")
                }else {
                    val message = when(error){
                        DataError.Network.NOT_FOUND -> "Пользователь с указанной почтой или паролем не найден"
                        DataError.Network.REQUEST_TIMEOUT -> "Время ожидание превышено. Проверьте интернет соединение или попробуйте позже"
                        DataError.Network.SERIALIZATION -> "При получении данных произошла ошибка"
                        DataError.Network.SERVER_ERROR -> "Произошла ошибка на сервере. Попробуйте позже"
                        DataError.Network.NO_INTERNET -> "Отсутствует интернет соединение"
                        DataError.Network.INVALID_DATA -> "Проверьте корректность введенных данных"
                        else -> "Произошла ошибка. Попробуйте позже"
                    }
                    snackbarManager.sendMessage(message)
                }
            }
            _state.update { it.copy(loginIsLoading = false) }
        }.onError { error, _ ->
            val message = when(error){
                EMAIL_EMPTY -> "Email пустой"
                PASSWORD_EMPTY -> "Password пустой"
            }
            snackbarManager.sendMessage(message)
        }
    }

    override suspend fun handleIntent(intent: LoginIntent) {
        when(intent){
            is LoginIntent.ChangeEmail -> changeEmail(intent.value)
            is LoginIntent.ChangePassword -> changePassword(intent.value)
            LoginIntent.SwitchShowPassword -> switchShowPassword()
            LoginIntent.ClickResetPassword -> {}
            LoginIntent.ClickSingUp -> {}
            LoginIntent.ClickLogin -> login()
        }
    }

}