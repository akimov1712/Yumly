package ru.topbun.data.source.remote.dto.account

import ru.topbun.domain.entity.account.ResetPasswordEntity

data class ResetPasswordRequest(
    val email: String,
    val newPassword: String
)

fun ResetPasswordEntity.toRequest() = ResetPasswordRequest(email, password)

