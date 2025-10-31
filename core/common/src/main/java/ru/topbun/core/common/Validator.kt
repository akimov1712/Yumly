package ru.topbun.core.common

import ru.topbun.core.common.error.ValidatorError

interface Validator<T> {

    fun validate(data: T): Result<Unit, ValidatorError>

}