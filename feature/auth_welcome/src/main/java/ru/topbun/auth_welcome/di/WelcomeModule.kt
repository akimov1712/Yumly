package ru.topbun.auth_welcome.di

import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module
import ru.topbun.auth_welcome.WelcomeViewModel

val welcomeModule = module {
    viewModelOf(::WelcomeViewModel)
}
