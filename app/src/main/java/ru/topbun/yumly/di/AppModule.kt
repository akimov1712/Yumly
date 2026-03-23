package ru.topbun.yumly.di

import org.koin.dsl.module

val appModule = module {
    includes(
        useCaseModule,
        featureModule,
        dataModule
    )
}
