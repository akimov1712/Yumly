package ru.topbun.data.di

import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.bind
import org.koin.dsl.module
import ru.topbun.data.repository.account.AccountRepositoryImpl
import ru.topbun.data.repository.config.ConfigRepositoryImpl
import ru.topbun.data.repository.favorite.FavoriteRepositoryImpl
import ru.topbun.data.repository.follow.FollowRepositoryImpl
import ru.topbun.data.repository.gpt.GptRepositoryImpl
import ru.topbun.data.repository.history.HistoryRepositoryImpl
import ru.topbun.data.repository.login.LoginRepositoryImpl
import ru.topbun.data.repository.notification.NotificationRepositoryImpl
import ru.topbun.data.repository.recipe.RecipeRepositoryImpl
import ru.topbun.data.repository.session.SessionRepositoryImpl
import ru.topbun.data.repository.signUp.SignUpRepositoryImpl
import ru.topbun.data.repository.upload.UploadRepositoryImpl
import ru.topbun.data.repository.verification.VerificationRepositoryImpl
import ru.topbun.domain.repository.account.AccountRepository
import ru.topbun.domain.repository.config.ConfigRepository
import ru.topbun.domain.repository.favorite.FavoriteRepository
import ru.topbun.domain.repository.follow.FollowRepository
import ru.topbun.domain.repository.gpt.GptRepository
import ru.topbun.domain.repository.history.HistoryRepository
import ru.topbun.domain.repository.login.LoginRepository
import ru.topbun.domain.repository.notification.NotificationRepository
import ru.topbun.domain.repository.recipe.RecipeRepository
import ru.topbun.domain.repository.session.SessionRepository
import ru.topbun.domain.repository.signUp.SignUpRepository
import ru.topbun.domain.repository.upload.UploadRepository
import ru.topbun.domain.repository.verification.VerificationRepository

val repositoryModule = module {
    factoryOf(::AccountRepositoryImpl) bind AccountRepository::class
    factoryOf(::ConfigRepositoryImpl) bind ConfigRepository::class
    factoryOf(::FavoriteRepositoryImpl) bind FavoriteRepository::class
    factoryOf(::FollowRepositoryImpl) bind FollowRepository::class
    factoryOf(::GptRepositoryImpl) bind GptRepository::class
    factoryOf(::SessionRepositoryImpl) bind SessionRepository::class
    factoryOf(::HistoryRepositoryImpl) bind HistoryRepository::class
    factoryOf(::LoginRepositoryImpl) bind LoginRepository::class
    factoryOf(::NotificationRepositoryImpl) bind NotificationRepository::class
    factoryOf(::RecipeRepositoryImpl) bind RecipeRepository::class
    factoryOf(::SignUpRepositoryImpl) bind SignUpRepository::class
    factoryOf(::UploadRepositoryImpl) bind UploadRepository::class
    factoryOf(::VerificationRepositoryImpl) bind VerificationRepository::class
}
