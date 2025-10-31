package ru.topbun.domain.validation.login

import ru.topbun.core.common.Result
import ru.topbun.core.common.Validator
import ru.topbun.core.common.error.ValidatorError
import ru.topbun.domain.entity.login.LoginEntity

class LoginValidator: Validator<LoginEntity> {

    override fun validate(data: LoginEntity): Result<Unit, ValidatorError> {
        val error = when{
            data.email.isEmpty() -> LoginValidatorError.EMAIL_EMPTY
            data.password.isEmpty() -> LoginValidatorError.PASSWORD_EMPTY
            else -> null
        }
        return error?.let { Result.Error(error) } ?: run { Result.Success(Unit) }
    }

}

enum class LoginValidatorError: ValidatorError{

    EMAIL_EMPTY, PASSWORD_EMPTY

}