package ru.topbun.profile_settings.di

import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module
import ru.topbun.profile_settings.ProfileSettingsViewModel

val profileSettingsModule = module {
    viewModelOf(::ProfileSettingsViewModel)
}
