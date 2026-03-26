package ru.topbun.auth_reset

import kotlinx.coroutines.flow.update
import ru.topbun.core.android.MVI

class ResetViewModel(
    private val email: String
): MVI<ResetIntent, ResetState, ResetEvent>(ResetState(email)){

    private fun changePassword(value: String){ if (value.length <= 64) _state.update { it.copy(password = value) } }
    private fun changeConfirmPassword(value: String){ if (value.length <= 64) _state.update { it.copy(confirmPassword = value) } }
    private fun switchShowPassword(){ _state.update { it.copy(showPassword = !_state.value.showPassword) } }

    override suspend fun handleIntent(intent: ResetIntent) {
        when(intent){
            is ResetIntent.ChangePassword -> changePassword(intent.value)
            is ResetIntent.ChangeConfirmPassword -> changeConfirmPassword(intent.value)
            ResetIntent.SwitchShowPassword -> switchShowPassword()
        }
    }

}