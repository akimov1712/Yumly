package ru.topbun.auth_login

internal data class LoginState(
    val email: String ="",
    val password: String = "",
    val showPassword: Boolean = false,
    val loginIsLoading: Boolean = false,
){

    val loginButtonEnabled: Boolean
        get() = email.isNotBlank() && password.isNotBlank()

}

internal sealed interface LoginIntent{

    data class ChangeEmail(val value: String): LoginIntent
    data class ChangePassword(val value: String): LoginIntent
    data object SwitchShowPassword: LoginIntent
    data object ClickSingUp: LoginIntent
    data object ClickResetPassword: LoginIntent
    data object ClickLogin: LoginIntent

}

internal sealed interface LoginEvent{

    data object NavigateToSignUp: LoginEvent
    data object NavigateToResetPassword: LoginEvent

}