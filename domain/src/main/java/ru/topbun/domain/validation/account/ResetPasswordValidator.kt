package ru.topbun.domain.validation.account

import ru.topbun.common.Result
import ru.topbun.common.Validator
import ru.topbun.common.error.ValidatorError
import ru.topbun.domain.entity.account.ResetPasswordEntity

class ResetPasswordValidator: Validator<ResetPasswordEntity> {

    override fun validate(data: ResetPasswordEntity): Result<Unit, ValidatorError> {
        return when{

            data.password.isBlank() ->
                Result.Error(ResetPasswordValidatorError.PASSWORD_EMPTY)

            data.password.length < 6 ->
                Result.Error(ResetPasswordValidatorError.PASSWORD_SHORT)

            data.password != data.confirmPassword ->
                Result.Error(ResetPasswordValidatorError.PASSWORD_NOT_MATCH)

            else -> Result.Success(Unit)

        }
    }

}

enum class ResetPasswordValidatorError: ValidatorError {

    PASSWORD_EMPTY, PASSWORD_SHORT, PASSWORD_NOT_MATCH,

}