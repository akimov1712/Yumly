package ru.topbun.data.source.remote.dto.signUp

import ru.topbun.domain.entity.signUp.SignUpEntity

data class SignUpRequest(
    val email: String,
    val username: String,
    val password: String,
    val photoUrl: String?
)

internal fun SignUpEntity.toRequest() = SignUpRequest(email, username, password, photoUrl)
