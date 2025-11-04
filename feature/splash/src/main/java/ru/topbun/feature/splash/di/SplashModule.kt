package ru.topbun.feature.splash.di

import cafe.adriel.voyager.core.registry.screenModule
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module
import ru.topbun.feature.splash.SplashScreen
import ru.topbun.feature.splash.SplashViewModel
import ru.topbun.navigation.RootScreenProvider

val splashScreenModule = screenModule {
    register<RootScreenProvider.Splash> {
        SplashScreen
    }
}

val splashModule = module {
    viewModel {
        SplashViewModel(get(), get())
    }
}