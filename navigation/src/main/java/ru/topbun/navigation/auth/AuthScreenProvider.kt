package ru.topbun.navigation.auth

import cafe.adriel.voyager.core.registry.ScreenProvider

sealed interface AuthScreenProvider : ScreenProvider {

    object Welcome : AuthScreenProvider
    object Login : AuthScreenProvider
    object Register : AuthScreenProvider
    object ResetRequest : AuthScreenProvider
    data class Confirm(val email: String, val screenMode: AuthConfirmMode) : AuthScreenProvider
    data class ResetNewPassword(val email: String, val code: String) : AuthScreenProvider

}
