package ru.topbun.yumly.di

import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module
import ru.topbun.domain.useCases.account.GetAccountInfoUseCase
import ru.topbun.domain.useCases.account.GetProfileUseCase
import ru.topbun.domain.useCases.account.ResetPasswordUseCase
import ru.topbun.domain.useCases.account.UpdateAccountInfoUseCase
import ru.topbun.domain.useCases.config.GetStatusFirstStartUseCase
import ru.topbun.domain.useCases.config.SetStatusFirstStartUseCase
import ru.topbun.domain.useCases.favorite.GetFavoriteRecipesUseCase
import ru.topbun.domain.useCases.favorite.SwitchFavoriteRecipeUseCase
import ru.topbun.domain.useCases.follow.GetFollowersUseCase
import ru.topbun.domain.useCases.follow.GetFollowingUseCase
import ru.topbun.domain.useCases.follow.SwitchFollowUserUseCase
import ru.topbun.domain.useCases.gpt.GetGptChatByIdUseCase
import ru.topbun.domain.useCases.gpt.GetGptChatsUseCase
import ru.topbun.domain.useCases.gpt.SendGptMessageUseCase
import ru.topbun.domain.useCases.history.GetHistoryUseCase
import ru.topbun.domain.useCases.history.GetTopQueriesUseCase
import ru.topbun.domain.useCases.login.LoginUseCase
import ru.topbun.domain.useCases.notification.GetNotificationsUseCase
import ru.topbun.domain.useCases.recipe.AddRecipeUseCase
import ru.topbun.domain.useCases.recipe.DeleteRecipeUseCase
import ru.topbun.domain.useCases.recipe.GetRecipeByIdUseCase
import ru.topbun.domain.useCases.recipe.GetRecipeByUserIdUseCase
import ru.topbun.domain.useCases.recipe.GetRecipeUseCase
import ru.topbun.domain.useCases.recipe.GetTagsUseCase
import ru.topbun.domain.useCases.session.HasSessionUseCase
import ru.topbun.domain.useCases.signUp.SignUpUseCase
import ru.topbun.domain.useCases.upload.UploadFileUseCase
import ru.topbun.domain.useCases.verification.ConfirmVerificationUseCase
import ru.topbun.domain.useCases.verification.RequestVerificationUseCase

val useCaseModule = module {
    singleOf(::GetAccountInfoUseCase)
    singleOf(::GetProfileUseCase)
    singleOf(::UpdateAccountInfoUseCase)
    singleOf(::GetStatusFirstStartUseCase)
    singleOf(::SetStatusFirstStartUseCase)
    singleOf(::GetFavoriteRecipesUseCase)
    singleOf(::SwitchFavoriteRecipeUseCase)
    singleOf(::GetFollowersUseCase)
    singleOf(::GetFollowingUseCase)
    singleOf(::SwitchFollowUserUseCase)
    singleOf(::GetGptChatByIdUseCase)
    singleOf(::GetGptChatsUseCase)
    singleOf(::SendGptMessageUseCase)
    singleOf(::GetHistoryUseCase)
    singleOf(::GetTopQueriesUseCase)
    singleOf(::LoginUseCase)
    singleOf(::GetNotificationsUseCase)
    singleOf(::AddRecipeUseCase)
    singleOf(::DeleteRecipeUseCase)
    singleOf(::GetRecipeByIdUseCase)
    singleOf(::GetRecipeByUserIdUseCase)
    singleOf(::GetRecipeUseCase)
    singleOf(::HasSessionUseCase)
    singleOf(::SignUpUseCase)
    singleOf(::UploadFileUseCase)
    singleOf(::ConfirmVerificationUseCase)
    singleOf(::RequestVerificationUseCase)
    singleOf(::ResetPasswordUseCase)
    singleOf(::GetTagsUseCase)
}
