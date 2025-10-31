package ru.topbun.domain.validation.signUp

import ru.topbun.core.common.Result
import ru.topbun.core.common.Validator
import ru.topbun.core.common.error.ValidatorError
import ru.topbun.core.common.isEmailValid
import ru.topbun.domain.entity.signUp.SignUpEntity

class SignUpValidator: Validator<SignUpEntity> {

    override fun validate(signUp: SignUpEntity): Result<Unit, ValidatorError> {
        val error = when{
            signUp.username.isBlank() -> SignUpValidatorError.USERNAME_EMPTY
            signUp.username.length < 4 -> SignUpValidatorError.USERNAME_SHORT

            signUp.email.isBlank() -> SignUpValidatorError.EMAIL_EMPTY
            !signUp.email.isEmailValid() -> SignUpValidatorError.EMAIL_NOT_VALID

            signUp.password.isBlank() -> SignUpValidatorError.PASSWORD_EMPTY
            signUp.password.length < 6 -> SignUpValidatorError.PASSWORD_SHORT
            signUp.password != signUp.confirmPassword -> SignUpValidatorError.PASSWORD_NOT_MATCH

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