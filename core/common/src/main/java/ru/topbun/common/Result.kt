package ru.topbun.common

typealias RootError = Error

sealed class Result<out D, out E: RootError>{

    data class Success<out D, out E: RootError>(val data: D): Result<D, E>()
    data class Error<out D, out E: RootError>(val error: E, val data: D?): Result<D, E>()

}