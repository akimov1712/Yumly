package ru.topbun.profile_followers.di

import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module
import ru.topbun.navigation.ProfileScreenProvider
import ru.topbun.profile_followers.FollowListViewModel

val followListModule = module {
    viewModel { (userId: Int, initialTab: ProfileScreenProvider.FollowsTab) ->
        FollowListViewModel(
            userId,
            initialTab,
            get(),
            get(),
            get(),
            get(),
        )
    }
}
