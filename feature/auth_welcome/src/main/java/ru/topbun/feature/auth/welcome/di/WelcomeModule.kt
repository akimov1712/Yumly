package ru.topbun.feature.auth.welcome.di

import cafe.adriel.voyager.core.registry.screenModule
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module
import ru.topbun.feature.auth.welcome.WelcomeScreen
import ru.topbun.feature.auth.welcome.WelcomeViewModel
import ru.topbun.navigation.auth.AuthScreenProvider

val welcomeModule = module {
    viewModelOf(::WelcomeViewModel)
}
