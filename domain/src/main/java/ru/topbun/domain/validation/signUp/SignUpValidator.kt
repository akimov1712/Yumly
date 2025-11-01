package ru.topbun.domain.validation.signUp

import ru.topbun.core.common.Result
import ru.topbun.core.common.Validator
import ru.topbun.core.common.error.ValidatorError
import ru.topbun.core.common.isEmailValid
import ru.topbun.domain.entity.signUp.SignUpEntity

class SignUpValidator: Validator<SignUpEntity> {

    override fun validate(data: SignUpEntity): Result<Unit, ValidatorError> {
        val error = when{
            data.username.isBlank() -> SignUpValidatorError.USERNAME_EMPTY
            data.username.length < 4 -> SignUpValidatorError.USERNAME_SHORT

            data.email.isBlank() -> SignUpValidatorError.EMAIL_EMPTY
            !data.email.isEmailValid() -> SignUpValidatorError.EMAIL_NOT_VALID

            data.password.isBlank() -> SignUpValidatorError.PASSWORD_EMPTY
            data.password.length < 6 -> SignUpValidatorError.PASSWORD_SHORT
            data.password != data.confirmPassword -> SignUpValidatorError.PASSWORD_NOT_MATCH

            else -> null
        }
        return error?.let { Result.Error(error) } ?: run { Result.Success(Unit) }
    }

}

enum class SignUpValidatorError: ValidatorError{

    USERNAME_SHORT, USERNAME_EMPTY,
    EMAIL_EMPTY, EMAIL_NOT_VALID,
    PASSWORD_EMPTY, PASSWORD_SHORT, PASSWORD_NOT_MATCH,

}