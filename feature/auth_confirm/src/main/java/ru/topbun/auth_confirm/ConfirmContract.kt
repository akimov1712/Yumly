package ru.topbun.auth_confirm

import ru.topbun.navigation.auth.AuthConfirmMode

data class ConfirmState(
    val email: String,
    val screenMode: AuthConfirmMode,
    val code: String = "",
    val timer: Int = 60,
    val sendAgainLoading: Boolean = false,
    val confirmLoading: Boolean = false
){

    val sendAgainEnabled: Boolean
        get() = timer <= 0

    val confirmEnabled: Boolean
        get() = code.length == 4

    fun formatTimer(): String {
        val minutes = timer / 60
        val seconds = timer % 60
        return String.format("%02d:%02d", minutes, seconds)
    }

}

sealed interface ConfirmIntent{
    object ClickSendAgain: ConfirmIntent
    object ClickConfirm: ConfirmIntent
    data class ChangeCode(val code: String): ConfirmIntent
}

sealed interface ConfirmEvent{
    object NavigateToDashboard: ConfirmEvent
    data class NavigateToResetPassword(val email: String): ConfirmEvent
}