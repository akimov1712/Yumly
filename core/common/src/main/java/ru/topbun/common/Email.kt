package ru.topbun.common

fun String.isEmailValid() = Regex("^\\w+@[a-zA-Z_]+?\\.[a-zA-Z]{2,3}$").matches(this)