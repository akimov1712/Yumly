package ru.topbun.auth_reset

data class ResetState(
    val email: String,
    val password: String = "",
    val confirmPassword: String = "",
    val showPassword: Boolean = false,
    val resetLoading: Boolean = false
){

    val resetButtonEnabled: Boolean
        get() = listOf(password, confirmPassword).all { it.isNotBlank() }

}

sealed interface ResetIntent{

    data class ChangePassword(val value: String): ResetIntent
    data class ChangeConfirmPassword(val value: String): ResetIntent
    data object SwitchShowPassword: ResetIntent
    data object ClickReset: ResetIntent

}

sealed interface ResetEvent{

    object NavigateToLogin: ResetEvent

}