package ru.topbun.auth_login.di

import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module
import ru.topbun.auth_login.LoginViewModel

val loginModule = module {
    viewModel { LoginViewModel(get(), get(), get()) }
}