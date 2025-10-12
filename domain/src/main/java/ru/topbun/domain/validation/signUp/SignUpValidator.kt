package ru.topbun.domain.validation.signUp

import ru.topbun.common.Result
import ru.topbun.common.Validator
import ru.topbun.common.error.ValidatorError
import ru.topbun.common.isEmailValid
import ru.topbun.domain.entity.signUp.SignUpEntity

class SignUpValidator: Validator<SignUpEntity> {

    override fun validate(signUp: SignUpEntity): Result<Unit, ValidatorError> {
        return when{
            signUp.username.isBlank() ->
                Result.Error(SignUpValidatorError.USERNAME_EMPTY)

            signUp.username.length < 4 ->
                Result.Error(SignUpValidatorError.USERNAME_SHORT)

            signUp.email.isBlank() ->
                Result.Error(SignUpValidatorError.EMAIL_EMPTY)

            !signUp.email.isEmailValid() ->
                Result.Error(SignUpValidatorError.EMAIL_NOT_VALID)

            signUp.password.isBlank() ->
                Result.Error(SignUpValidatorError.PASSWORD_EMPTY)

            signUp.password.length < 6 ->
                Result.Error(SignUpValidatorError.PASSWORD_SHORT)

            signUp.password != signUp.confirmPassword -> 
                Result.Error(SignUpValidatorError.PASSWORD_NOT_MATCH)

            else -> return Result.Success(Unit)
        }
    }

}

enum class SignUpValidatorError: ValidatorError{

    USERNAME_SHORT, USERNAME_EMPTY,
    EMAIL_EMPTY, EMAIL_NOT_VALID,
    PASSWORD_EMPTY, PASSWORD_SHORT, PASSWORD_NOT_MATCH,

}