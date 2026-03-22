package ru.topbun.yumly.di

import org.koin.dsl.module
import ru.topbun.data.di.repositoryModule

val appModule = module {
    includes(
        repositoryModule,
        useCaseModule,
        featureModule
    )
}
