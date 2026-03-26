package ru.topbun.auth_reset

data class ResetState(
    val email: String,
    val password: String = "",
    val confirmPassword: String = "",
    val showPassword: Boolean = false,
)

sealed interface ResetIntent{

    data class ChangePassword(val value: String): ResetIntent
    data class ChangeConfirmPassword(val value: String): ResetIntent
    data object SwitchShowPassword: ResetIntent

}

sealed interface ResetEvent{

    object NavigateToDashboard: ResetEvent

}