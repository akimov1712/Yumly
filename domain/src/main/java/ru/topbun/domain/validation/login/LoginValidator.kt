package ru.topbun.domain.validation.login

import ru.topbun.common.Result
import ru.topbun.common.Validator
import ru.topbun.common.error.ValidatorError
import ru.topbun.domain.entity.login.LoginEntity

class LoginValidator: Validator<LoginEntity> {

    override fun validate(data: LoginEntity): Result<Unit, ValidatorError> {
        return when{
            data.email.isEmpty() -> Result.Error(LoginValidatorError.EMAIL_EMPTY)
            data.password.isEmpty() -> Result.Error(LoginValidatorError.PASSWORD_EMPTY)
            else -> Result.Success(Unit)
        }
    }

}

enum class LoginValidatorError: ValidatorError{

    EMAIL_EMPTY, PASSWORD_EMPTY

}