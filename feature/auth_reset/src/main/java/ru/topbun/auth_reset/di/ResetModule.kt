package ru.topbun.auth_reset.di

import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module
import ru.topbun.auth_reset.ResetViewModel

val resetModule = module {
    viewModelOf(::ResetViewModel)
}