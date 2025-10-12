package ru.topbun.data.source.remote.dto.login

import android.R.attr.data
import ru.topbun.domain.entity.login.LoginEntity

data class LoginRequest(
    val email: String,
    val password: String
)

fun LoginEntity.toRequest() = LoginRequest(email, password)


