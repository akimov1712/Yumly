package ru.topbun.domain.validation.account

import ru.topbun.core.common.Result
import ru.topbun.core.common.Validator
import ru.topbun.core.common.error.ValidatorError
import ru.topbun.domain.entity.account.UpdateAccountInfoEntity

class UpdateAccountInfoValidator: Validator<UpdateAccountInfoEntity> {


    override fun validate(data: UpdateAccountInfoEntity): Result<Unit, ValidatorError> {
        val error = when{
            data.username.length < 4 -> UpdateAccountInfoValidatorError.USERNAME_SHORT
            else -> null
        }
        return error?.let { Result.Error(error) } ?: run { Result.Success(Unit) }
    }


}

enum class UpdateAccountInfoValidatorError: ValidatorError{

    USERNAME_SHORT

}