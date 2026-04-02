package ru.topbun.yumly.di

import org.koin.dsl.module
import ru.topbun.auth_confirm.di.confirmModule
import ru.topbun.auth_login.di.loginModule
import ru.topbun.auth_register.di.registerModule
import ru.topbun.auth_reset.di.resetModule
import ru.topbun.auth_reset_request.di.resetRequestModule
import ru.topbun.auth_welcome.di.welcomeModule
import ru.topbun.core.android.snackbarModule
import ru.topbun.feature.splash.di.splashModule
import ru.topbun.home.di.homeModule

val featureModule = module {
    includes(
        snackbarModule,
        welcomeModule,
        splashModule,
        loginModule,
        registerModule,
        resetRequestModule,
        confirmModule,
        resetModule,
        homeModule
    )
}
