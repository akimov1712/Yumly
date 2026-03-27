package ru.topbun.auth_register

internal data class RegisterState(
    val username: String = "",
    val email: String = "",
    val password: String = "",
    val confirmPassword: String = "",
    val showPassword: Boolean = false,
    val fieldFocused: FieldFocused? = null,
    val registerIsLoading: Boolean = false,

    val usernameError: String? = null,
    val emailError: String? = null,
    val passwordError: String? = null,
    val confirmPasswordError: String? = null,
){

    val registerButtonEnabled: Boolean
        get() = listOf(username, email, password, confirmPassword).all { it.isNotBlank() }


    enum class FieldFocused{
        USERNAME, EMAIL, PASSWORD, CONFIRM_PASSWORD
    }

}

internal sealed interface RegisterIntent{
    data class ChangeUsername(val value: String): RegisterIntent
    data class ChangeEmail(val value: String): RegisterIntent
    data class ChangePassword(val value: String): RegisterIntent
    data class ChangeConfirmPassword(val value: String): RegisterIntent
    data class ChangeFieldFocused(val field: RegisterState.FieldFocused): RegisterIntent
    data object SwitchShowPassword: RegisterIntent
    object ClickSignUp : RegisterIntent
    object ClickLogin : RegisterIntent
}

internal sealed interface RegisterEvent{
    object NavigateToLogin: RegisterEvent
    data class NavigateToConfirm(val email: String): RegisterEvent
}