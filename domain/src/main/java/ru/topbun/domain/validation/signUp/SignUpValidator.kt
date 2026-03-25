package ru.topbun.domain.validation.signUp

import ru.topbun.core.common.Result
import ru.topbun.core.common.Validator
import ru.topbun.core.common.error.ValidatorError
import ru.topbun.core.common.isEmailValid
import ru.topbun.domain.entity.signUp.SignUpEntity

class SignUpValidator : Validator<SignUpEntity> {

    override fun validate(data: SignUpEntity): Result<Unit, SignUpValidationError> {

        val errors = mutableMapOf<SignUpValidatorField, MutableList<SignUpValidatorError>>()

        fun addError(field: SignUpValidatorField, error: SignUpValidatorError) {
            errors.getOrPut(field) { mutableListOf() }.add(error)
        }

        if (data.username.isBlank()) {
            addError(SignUpValidatorField.USERNAME, SignUpValidatorError.USERNAME_EMPTY)
        } else if (data.username.length < 4) {
            addError(SignUpValidatorField.USERNAME, SignUpValidatorError.USERNAME_SHORT)
        }

        if (data.email.isBlank()) {
            addError(SignUpValidatorField.EMAIL, SignUpValidatorError.EMAIL_EMPTY)
        } else if (!data.email.isEmailValid()) {
            addError(SignUpValidatorField.EMAIL, SignUpValidatorError.EMAIL_NOT_VALID)
        }

        if (data.password.isBlank()) {
            addError(SignUpValidatorField.PASSWORD, SignUpValidatorError.PASSWORD_EMPTY)
        } else if (data.password.length < 6) {
            addError(SignUpValidatorField.PASSWORD, SignUpValidatorError.PASSWORD_SHORT)
        }

        if (data.password != data.confirmPassword) {
            addError(SignUpValidatorField.CONFIRM_PASSWORD, SignUpValidatorError.PASSWORD_NOT_MATCH)
        }

        return if (errors.isEmpty()) {
            Result.Success(Unit)
        } else {
            Result.Error(SignUpValidationError(errors))
        }
    }
}

class SignUpValidationError(
    val errors: Map<SignUpValidatorField, List<SignUpValidatorError>>
) : ValidatorError

enum class SignUpValidatorError(val message: String){

    USERNAME_EMPTY("Имя не может быть пустым"),
    USERNAME_SHORT("Минимальная длина имени 4 символа"),
    EMAIL_EMPTY("Почта не может быть пустой"),
    EMAIL_NOT_VALID("Почта неверного формата. Попробуйте в виде example@gmail.com"),
    PASSWORD_EMPTY("Пароль не может быть пустым"),
    PASSWORD_SHORT("Минимальная длина пароля 6 символов"),
    PASSWORD_NOT_MATCH("Пароли не совпадают"),

}

enum class SignUpValidatorField{
    USERNAME, EMAIL, PASSWORD, CONFIRM_PASSWORD
}