package ru.topbun.common

import ru.topbun.common.error.ValidatorError

interface Validator<T> {

    fun validate(data: T): Result<Unit, ValidatorError>

}