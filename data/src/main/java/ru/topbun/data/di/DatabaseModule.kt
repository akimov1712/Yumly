package ru.topbun.data.di

import org.koin.dsl.module
import ru.topbun.data.source.local.database.AppDatabase

val databaseModule = module {
    single { AppDatabase.getInstance(get()) }
    single { get<AppDatabase>().historyDao() }
    single { get<AppDatabase>().favoriteRecipeDao() }
}
