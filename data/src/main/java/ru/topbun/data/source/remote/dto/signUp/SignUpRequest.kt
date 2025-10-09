package ru.topbun.data.source.remote.dto.signUp

data class SignUpRequest(
    val email: String,
    val username: String,
    val password: String,
    val photoUrl: String?
)