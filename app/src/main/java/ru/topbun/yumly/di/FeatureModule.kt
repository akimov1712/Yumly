package ru.topbun.yumly.di

import org.koin.dsl.module
import ru.topbun.feature.auth.welcome.di.welcomeModule
import ru.topbun.feature.splash.di.splashModule

val featureModule = module {
    includes(
        welcomeModule,
        splashModule
    )
}
