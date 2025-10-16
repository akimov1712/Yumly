package ru.topbun.data.source.remote.dto.login

import android.R.attr.data
import ru.topbun.domain.entity.login.LoginEntity

internal data class LoginRequest(
    val email: String,
    val password: String
)

internal fun LoginEntity.toRequest() = LoginRequest(email, password)


