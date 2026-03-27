package ru.topbun.auth_register

import android.R.id.message
import kotlinx.coroutines.flow.update
import ru.topbun.core.android.MVI
import ru.topbun.core.android.SnackbarManager
import ru.topbun.core.common.error.DataError
import ru.topbun.core.common.onError
import ru.topbun.core.common.onSuccess
import ru.topbun.domain.entity.signUp.SignUpEntity
import ru.topbun.domain.entity.verification.RequestVerificationEntity
import ru.topbun.domain.entity.verification.VerificationType
import ru.topbun.domain.useCases.signUp.SignUpUseCase
import ru.topbun.domain.useCases.verification.RequestVerificationUseCase
import ru.topbun.domain.validation.signUp.SignUpValidator

internal class RegisterViewModel(
    private val signUpValidator: SignUpValidator,
    private val signUpUseCase: SignUpUseCase,
    private val snackbarManager: SnackbarManager,
    private val requestVerificationUseCase: RequestVerificationUseCase
) : MVI<RegisterIntent, RegisterState, RegisterEvent>(RegisterState()) {

    private fun changeUsername(value: String) {
        if (value.length < 64) _state.update { it.copy(username = value) }
    }

    private fun changeEmail(value: String) {
        if (value.length < 128) _state.update { it.copy(email = value) }
    }

    private fun changePassword(value: String) {
        if (value.length < 64) _state.update { it.copy(password = value) }
    }

    private fun changeConfirmPassword(value: String) {
        if (value.length < 64) _state.update { it.copy(confirmPassword = value) }
    }

    private fun changeFieldFocused(field: RegisterState.FieldFocused) =
        _state.update { it.copy(fieldFocused = field) }

    private fun switchShowPassword() =
        _state.update { it.copy(showPassword = !_state.value.showPassword) }

    private suspend fun navigateLogin() = _events.send(RegisterEvent.NavigateToLogin)

    private suspend fun singUp(): Unit = with(state.value) {
        val signUp = SignUpEntity(
            username = username,
            email = email,
            password = password,
            confirmPassword = confirmPassword
        )
        val resultValidation = signUpValidator.validate(signUp)
        resultValidation.onSuccess {
            _state.update { it.copy(registerIsLoading = true) }
            val result = signUpUseCase(signUp)
            result.onSuccess { user ->
                val verification = RequestVerificationEntity(user.email, VerificationType.SIGN_UP_CONFIRM)
                requestVerificationUseCase(verification).onSuccess {
                    _events.send(RegisterEvent.NavigateToConfirm(user.email))
                }.onError { _, _ ->
                    snackbarManager.showMessage("Произошла ошибка. Попробуйте позже")
                }
            }.onError { error, _ ->
                val message = when (error) {
                    DataError.Network.INVALID_DATA -> "Проверьте корректность введеных данных"
                    DataError.Network.EXISTS -> "Пользователь с указанной почтой или паролем уже зарегистрирован"
                    DataError.Network.REQUEST_TIMEOUT -> "Время ожидание превышено. Проверьте интернет соединение или попробуйте позже"
                    DataError.Network.SERIALIZATION -> "При получении данных произошла ошибка"
                    DataError.Network.SERVER_ERROR -> "Произошла ошибка на сервере. Попробуйте позже"
                    DataError.Network.NO_INTERNET -> "Отсутствует интернет соединение"
                    else -> "Произошла ошибка. Попробуйте позже"
                }
                snackbarManager.showMessage(message)
            }
            _state.update { it.copy(registerIsLoading = false) }

        }.onError { error, _ ->
            error.errors.forEach { (_, fieldError) ->
                if (fieldError.isNotEmpty()) {
                    val message = fieldError.firstOrNull()
                    message?.let {
                        snackbarManager.showMessage(it.message)
                        return@onError
                    }
                }
            }
        }
    }

    override suspend fun handleIntent(intent: RegisterIntent) {
        when (intent) {
            is RegisterIntent.ChangeUsername -> changeUsername(intent.value)
            is RegisterIntent.ChangeEmail -> changeEmail(intent.value)
            is RegisterIntent.ChangePassword -> changePassword(intent.value)
            is RegisterIntent.ChangeConfirmPassword -> changeConfirmPassword(intent.value)
            RegisterIntent.SwitchShowPassword -> switchShowPassword()
            is RegisterIntent.ChangeFieldFocused -> changeFieldFocused(intent.field)
            RegisterIntent.ClickLogin -> navigateLogin()
            RegisterIntent.ClickSignUp -> singUp()
        }
    }



}