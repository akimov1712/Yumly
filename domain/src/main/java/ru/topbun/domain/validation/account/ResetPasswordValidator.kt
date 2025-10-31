package ru.topbun.domain.validation.account

import ru.topbun.core.common.Result
import ru.topbun.core.common.Validator
import ru.topbun.core.common.error.ValidatorError
import ru.topbun.domain.entity.account.ResetPasswordEntity

class ResetPasswordValidator: Validator<ResetPasswordEntity> {

    override fun validate(data: ResetPasswordEntity): Result<Unit, ValidatorError> {
        val error =  when{
            data.password.isBlank() -> ResetPasswordValidatorError.PASSWORD_EMPTY
            data.password.length < 6 -> ResetPasswordValidatorError.PASSWORD_SHORT
            data.password != data.confirmPassword -> ResetPasswordValidatorError.PASSWORD_NOT_MATCH
            else -> null
        }
        return error?.let { Result.Error(error) } ?: run { Result.Success(Unit) }
    }

}

enum class ResetPasswordValidatorError: ValidatorError {

    PASSWORD_EMPTY, PASSWORD_SHORT, PASSWORD_NOT_MATCH,

}