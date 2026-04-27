package ru.topbun.profile_followers

import androidx.compose.foundation.lazy.LazyListState
import ru.topbun.domain.ScreenUiState
import ru.topbun.domain.entity.account.ProfileEntity
import ru.topbun.navigation.ProfileScreenProvider

internal data class FollowListState(
    val userId: Int,
    val targetProfile: ProfileEntity? = null,
    val profileStatus: ScreenUiState = ScreenUiState.Idle,
    val selectedTab: ProfileScreenProvider.FollowsTab = ProfileScreenProvider.FollowsTab.Followers,
    val followersList: ListUiState = ListUiState(),
    val followingList: ListUiState = ListUiState(),
    val followersListState: LazyListState = LazyListState(),
    val followingListState: LazyListState = LazyListState(),
) {

    val visibleList: ListUiState
        get() = if (selectedTab == ProfileScreenProvider.FollowsTab.Followers) followersList else followingList

    val visibleListState: LazyListState
        get() = if (selectedTab == ProfileScreenProvider.FollowsTab.Followers) followersListState else followingListState

    data class ListUiState(
        val users: List<ProfileEntity> = emptyList(),
        val status: ScreenUiState = ScreenUiState.Idle,
        val isEndList: Boolean = false,
    )

}

internal sealed interface FollowListIntent {

    data object LoadProfile : FollowListIntent
    data object Refresh : FollowListIntent
    data object LoadFollowers : FollowListIntent
    data object LoadFollowing : FollowListIntent
    data class ChangeTab(val tab: ProfileScreenProvider.FollowsTab) : FollowListIntent

}

internal sealed interface FollowListEvent
