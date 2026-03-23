package ru.topbun.data.di

import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module
import ru.topbun.data.source.local.config.Config
import ru.topbun.data.source.local.config.Config.dataStore
import ru.topbun.data.source.local.config.CryptConfig
import ru.topbun.data.source.local.config.DataStoreManager
import ru.topbun.data.source.local.config.DefaultConfig
import ru.topbun.data.source.local.config.TokenManager

val configModule = module {
    factory<CryptConfig> { Config.createCryptConfig(get()) }
    factory<DefaultConfig> { androidContext().dataStore }
    factoryOf(::DataStoreManager)
    factoryOf(::TokenManager)
}