package ru.topbun.profile

import androidx.compose.foundation.lazy.LazyListState
import ru.topbun.domain.ScreenUiState
import ru.topbun.domain.entity.account.ProfileEntity
import ru.topbun.domain.entity.account.UserEntity
import ru.topbun.domain.entity.recipe.RecipeEntity

internal data class ProfileState(
    val mode: Mode = Mode.Self,
    val profileUiState: ProfileUiState? = null,
    val account: UserEntity? = null,
    val profile: ProfileEntity? = null,
    val profileStatus: ScreenUiState = ScreenUiState.Idle,
    val selectedTab: ProfileTab = ProfileTab.MyRecipes,
    val recipeList: RecipeListUiState = RecipeListUiState(),
    val recipeListState: LazyListState = LazyListState(),
    val likedList: RecipeListUiState = RecipeListUiState(),
    val likedListState: LazyListState = LazyListState(),
    val showSettingsDialog: Boolean = false,
    val showLogoutDialog: Boolean = false,
    val followLoading: Boolean = false,
) {

    val isSelf: Boolean
        get() = mode is Mode.Self

    val targetUserId: Int?
        get() = when (mode) {
            is Mode.Self -> account?.id
            is Mode.Other -> mode.userId
        }

    val visibleList: RecipeListUiState
        get() = if (selectedTab == ProfileTab.MyRecipes) recipeList else likedList

    val visibleListState: LazyListState
        get() = if (selectedTab == ProfileTab.MyRecipes) recipeListState else likedListState

    val showProfileError: Boolean
        get() = profile == null && profileStatus == ScreenUiState.Error

    sealed interface Mode {
        data object Self : Mode
        data class Other(val userId: Int) : Mode
    }

    enum class ProfileUiState {
        SUCCESS, NEED_AUTH
    }

    enum class ProfileTab(val title: String) {
        MyRecipes("Рецепты"),
        Liked("Лайки");
    }

    data class RecipeListUiState(
        val recipes: List<RecipeEntity> = emptyList(),
        val status: ScreenUiState = ScreenUiState.Idle,
        val isEndList: Boolean = false,
        val isFromCache: Boolean = false,
    )

}

internal sealed interface ProfileIntent {

    data object CheckSession : ProfileIntent
    data object LoadProfile : ProfileIntent
    data object Refresh : ProfileIntent
    data object LoadRecipes : ProfileIntent
    data object LoadLiked : ProfileIntent
    data object SwitchFollow : ProfileIntent
    data object Logout : ProfileIntent
    data class ChangeTab(val tab: ProfileState.ProfileTab) : ProfileIntent
    data class ChangeShowSettingsDialog(val value: Boolean) : ProfileIntent
    data class ChangeShowLogoutDialog(val value: Boolean) : ProfileIntent

}

internal sealed interface ProfileEvent {

    data object NavigateToAuth : ProfileEvent
    data object LoggedOut : ProfileEvent
    data object NavigateToBmi : ProfileEvent
    data class OpenUrl(val url: String) : ProfileEvent

}
