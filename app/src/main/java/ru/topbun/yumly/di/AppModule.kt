package ru.topbun.yumly.di

import org.koin.dsl.module

val appModule = module {
    includes(
        repositoryModule,
        useCaseModule,
        featureModule
    )
}
