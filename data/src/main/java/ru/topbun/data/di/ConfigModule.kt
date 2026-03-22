package ru.topbun.data.di

import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module
import ru.topbun.data.source.local.config.Config
import ru.topbun.data.source.local.config.Config.dataStore
import ru.topbun.data.source.local.config.CryptConfig
import ru.topbun.data.source.local.config.DataStoreManager
import ru.topbun.data.source.local.config.DefaultConfig
import ru.topbun.data.source.remote.ApiFactory

val configModule = module {
    single<CryptConfig> { Config.createCryptConfig(get()) }
    single<DefaultConfig> { androidContext().dataStore }
    singleOf(::DataStoreManager)
    singleOf(::ApiFactory)
}