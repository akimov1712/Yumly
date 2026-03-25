package ru.topbun.auth_register.di

import androidx.lifecycle.viewmodel.compose.viewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module
import ru.topbun.auth_register.RegisterViewModel

val registerModule = module {
    viewModelOf(::RegisterViewModel)
}