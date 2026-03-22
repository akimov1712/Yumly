package ru.topbun.yumly.di

import org.koin.dsl.module
import ru.topbun.data.di.apiModule
import ru.topbun.data.di.configModule
import ru.topbun.data.di.databaseModule
import ru.topbun.data.di.repositoryModule

val dataModule = module {
    includes(
        apiModule,
        configModule,
        databaseModule,
        repositoryModule
    )
}