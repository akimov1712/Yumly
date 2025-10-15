package ru.topbun.domain.validation.account

import ru.topbun.common.Result
import ru.topbun.common.Validator
import ru.topbun.common.error.ValidatorError
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