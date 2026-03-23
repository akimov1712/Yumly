package ru.topbun.core.common

fun String.isEmailValid() = Regex("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$").matches(this)
