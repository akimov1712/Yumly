package ru.topbun.auth_reset_request

import ru.topbun.domain.entity.verification.VerificationType

internal data class ResetRequestState(
    val email: String = "",
    val isLoading: Boolean = false,
){
    val buttonIsEnabled: Boolean
        get() = email.isNotBlank()

}

internal sealed interface ResetRequestIntent{

    data class ChangeEmail(val value: String): ResetRequestIntent
    data object ClickReset: ResetRequestIntent

}

internal sealed interface ResetRequestEvent{

    data class NavigateToConfirmReset(val email: String, val type: VerificationType): ResetRequestEvent

}