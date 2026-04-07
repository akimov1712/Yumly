package ru.topbun.domain

enum class ScreenUiState {
    Idle, Loading, Success, Error;

    val isLoading: Boolean
        get() = this == Loading

    val isError: Boolean
        get() = this == Error

    val isSuccess: Boolean
        get() = this == Success
}