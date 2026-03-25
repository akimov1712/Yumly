package ru.topbun.auth_reset_request.di

import androidx.lifecycle.viewmodel.compose.viewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module
import ru.topbun.auth_reset_request.ResetRequestViewModel

val resetRequestModule = module {
    viewModelOf(::ResetRequestViewModel)
}