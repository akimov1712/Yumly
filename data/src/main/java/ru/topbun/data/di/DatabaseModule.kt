package ru.topbun.data.di

import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module
import ru.topbun.data.source.local.database.AppDatabase
import ru.topbun.data.source.remote.ApiFactory
import ru.topbun.data.source.remote.AuthTokenInterceptor
import ru.topbun.data.source.remote.api.account.AccountApi
import ru.topbun.data.source.remote.api.favorite.FavoriteApi
import ru.topbun.data.source.remote.api.follow.FollowApi
import ru.topbun.data.source.remote.api.gpt.GptApi
import ru.topbun.data.source.remote.api.history.HistoryApi
import ru.topbun.data.source.remote.api.login.LoginApi
import ru.topbun.data.source.remote.api.notification.NotificationApi
import ru.topbun.data.source.remote.api.recipe.RecipeApi
import ru.topbun.data.source.remote.api.signUp.SignUpApi
import ru.topbun.data.source.remote.api.verification.VerificationApi

val databaseModule = module {
    single { AppDatabase.getInstance(get()) }
    single { get<AppDatabase>().historyDao() }
}