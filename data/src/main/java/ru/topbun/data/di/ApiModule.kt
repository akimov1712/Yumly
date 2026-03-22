package ru.topbun.data.di

import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module
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

val apiModule = module {
    singleOf(::AuthTokenInterceptor)
    singleOf(::ApiFactory)
    single<AccountApi>{ get<ApiFactory>().accountApi }
    single<FavoriteApi>{ get<ApiFactory>().favoriteApi }
    single<FollowApi>{ get<ApiFactory>().followApi }
    single<GptApi>{ get<ApiFactory>().gptApi }
    single<HistoryApi>{ get<ApiFactory>().historyApi }
    single<LoginApi>{ get<ApiFactory>().loginApi }
    single<NotificationApi>{ get<ApiFactory>().notificationApi }
    single<RecipeApi>{ get<ApiFactory>().recipeApi }
    single<SignUpApi>{ get<ApiFactory>().signUpApi }
    single<VerificationApi>{ get<ApiFactory>().verificationApi }
}