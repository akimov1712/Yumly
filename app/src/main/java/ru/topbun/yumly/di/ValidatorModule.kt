package ru.topbun.yumly.di

import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module
import ru.topbun.domain.validation.account.ResetPasswordValidator
import ru.topbun.domain.validation.account.UpdateAccountInfoValidator
import ru.topbun.domain.validation.login.LoginValidator
import ru.topbun.domain.validation.recipe.AddRecipeValidator
import ru.topbun.domain.validation.signUp.SignUpValidator

val validatorModule = module {
    singleOf(::ResetPasswordValidator)
    singleOf(::UpdateAccountInfoValidator)
    singleOf(::LoginValidator)
    singleOf(::AddRecipeValidator)
    singleOf(::SignUpValidator)
}