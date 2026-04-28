package ru.topbun.profile.di

import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module
import ru.topbun.profile.ProfileState
import ru.topbun.profile.ProfileViewModel

val profileModule = module {
    viewModel { (mode: ProfileState.Mode) ->
        ProfileViewModel(
            mode,
            get(),
            get(),
            get(),
            get(),
            get(),
            get(),
            get(),
            get(),
            get()
        )
    }
}
