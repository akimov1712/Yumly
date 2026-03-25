package ru.topbun.auth_register

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.topbun.core.android.MVI
import ru.topbun.core.android.SnackbarManager
import ru.topbun.core.common.onError
import ru.topbun.core.common.onSuccess
import ru.topbun.domain.entity.signUp.SignUpEntity
import ru.topbun.domain.useCases.signUp.SignUpUseCase
import ru.topbun.domain.validation.signUp.SignUpValidator
import ru.topbun.domain.validation.signUp.SignUpValidatorField.*

internal class RegisterViewModel(
    private val signUpValidator: SignUpValidator,
    private val signUpUseCase: SignUpUseCase,
    private val snackbarManager: SnackbarManager,
): MVI<RegisterIntent, RegisterState, RegisterEvent>(RegisterState()) {

    private fun changeUsername(value: String){ if (value.length < 64) _state.update { it.copy(username = value) } }
    private fun changeEmail(value: String){ if (value.length < 128) _state.update { it.copy(email = value) } }
    private fun changePassword(value: String){ if (value.length < 64) _state.update { it.copy(password = value) } }
    private fun changeConfirmPassword(value: String){ if (value.length < 64) _state.update { it.copy(confirmPassword = value) } }
    private fun changeFieldFocused(field: RegisterState.FieldFocused) = _state.update { it.copy(fieldFocused = field) }
    private fun switchShowPassword() = _state.update { it.copy(showPassword = !_state.value.showPassword) }
    private suspend fun navigateLogin() = _events.send(RegisterEvent.NavigateToLogin)

    private suspend fun singUp(): Unit = with(state.value){
        val signUp = SignUpEntity(
            username = username,
            email = email,
            password = password,
            confirmPassword = confirmPassword
        )
        val resultValidation = signUpValidator.validate(signUp)
        resultValidation.onSuccess {

        }.onError { error, _ ->
            error.errors.forEach { (_, fieldError) ->
                if (fieldError.isNotEmpty()){
                    val message = fieldError.firstOrNull()
                    message?.let {
                        snackbarManager.sendMessage(it.message)
                        return@onError
                    }
                }
            }
        }
    }

    override suspend fun handleIntent(intent: RegisterIntent) {
        when(intent){
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


    private fun validateField(username: String, email: String, password: String, confirmPassword: String){
        val signUp = SignUpEntity(
            username = username,
            email = email,
            password = password,
            confirmPassword = confirmPassword
        )
        val resultValidation = signUpValidator.validate(signUp)
        resultValidation.onError { error, _ ->
            error.errors.forEach { (field, fieldErrors) ->
                when(field){
                    USERNAME -> _state.update { it.copy(usernameError = fieldErrors.firstOrNull()?.message) }
                    EMAIL -> _state.update { it.copy(emailError = fieldErrors.firstOrNull()?.message) }
                    PASSWORD -> _state.update { it.copy(passwordError = fieldErrors.firstOrNull()?.message) }
                    CONFIRM_PASSWORD -> _state.update { it.copy(confirmPasswordError = fieldErrors.firstOrNull()?.message) }
                }
            }
        }
    }

}