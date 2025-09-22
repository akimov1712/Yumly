package ru.topbun.domain.entity.signUp

data class SignUpEntity(
    val email: String,
    val username: String,
    val password: String,
    val photoUrl: String? = null
)