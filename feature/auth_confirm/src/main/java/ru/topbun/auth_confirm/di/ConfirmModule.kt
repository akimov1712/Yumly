package ru.topbun.auth_confirm.di

import androidx.lifecycle.viewmodel.compose.viewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module
import ru.topbun.auth_confirm.ConfirmViewModel
import ru.topbun.navigation.auth.AuthConfirmMode

val confirmModule = module {
    viewModel<ConfirmViewModel> { (email: String, screenMode: AuthConfirmMode) -> ConfirmViewModel(email, screenMode, get(), get(), get()) }
}