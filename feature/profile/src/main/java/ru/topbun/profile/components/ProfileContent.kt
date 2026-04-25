package ru.topbun.profile.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import ru.topbun.domain.ScreenUiState
import ru.topbun.profile.ProfileIntent
import ru.topbun.profile.ProfileState
import ru.topbun.profile.ProfileViewModel

@Composable
internal fun ProfileContent(
    viewModel: ProfileViewModel,
    showBack: Boolean,
    onBack: () -> Unit,
    onNavigateToSettings: () -> Unit,
) {
    val state by viewModel.state.collectAsState()

    Column(modifier = Modifier.fillMaxSize()) {
        ProfileHeader(
            title = state.profile?.username ?: if (state.isSelf) "Профиль" else "",
            showBack = showBack,
            showSettings = state.isSelf,
            onClickBack = onBack,
            onClickSettings = {
                viewModel.sendIntent(ProfileIntent.ChangeShowSettingsDialog(true))
            }
        )

        when {
            state.profileStatus == ScreenUiState.Loading && state.profile == null -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.TopCenter
                ) {
                    ProfileShimmer()
                }
            }

            state.profile != null -> {
                ProfileRecipeList(
                    state = state.visibleListState,
                    listState = state.visibleList.status,
                    isEndList = state.visibleList.isEndList,
                    recipes = state.visibleList.recipes,
                    emptyTitle = if (state.selectedTab == ProfileState.ProfileTab.MyRecipes) {
                        "Рецептов пока нет"
                    } else {
                        "Лайкнутых рецептов нет"
                    },
                    emptyDescription = if (state.selectedTab == ProfileState.ProfileTab.MyRecipes) {
                        if (state.isSelf) "Опубликуй свой первый рецепт"
                        else "Этот пользователь ещё не публиковал рецептов"
                    } else {
                        "Лайкни рецепты, чтобы они появились здесь"
                    },
                    headerContent = {
                        ProfileInfo(
                            profile = state.profile,
                            isSelf = state.isSelf,
                            countRecipes = state.countRecipes,
                            followLoading = state.followLoading,
                            onClickFollow = { viewModel.sendIntent(ProfileIntent.SwitchFollow) },
                            onClickFollowers = { /* В будущем: экран подписчиков */ },
                            onClickFollowing = { /* В будущем: экран подписок */ }
                        )
                    },
                    tabsContent = {
                        ProfileTabsBar(
                            selectedTab = state.selectedTab,
                            onSelect = { viewModel.sendIntent(ProfileIntent.ChangeTab(it)) }
                        )
                    },
                    onRefresh = { viewModel.sendIntent(ProfileIntent.Refresh) },
                    onLoadMore = {
                        val intent = if (state.selectedTab == ProfileState.ProfileTab.MyRecipes) {
                            ProfileIntent.LoadRecipes
                        } else {
                            ProfileIntent.LoadLiked
                        }
                        viewModel.sendIntent(intent)
                    }
                )
            }
        }
    }

    if (state.showSettingsDialog) {
        SettingsDialog(
            onDismissRequest = {
                viewModel.sendIntent(ProfileIntent.ChangeShowSettingsDialog(false))
            },
            onClickEditProfile = {
                viewModel.sendIntent(ProfileIntent.ChangeShowSettingsDialog(false))
                onNavigateToSettings()
            },
            onClickLogout = {
                viewModel.sendIntent(ProfileIntent.ChangeShowSettingsDialog(false))
                viewModel.sendIntent(ProfileIntent.ChangeShowLogoutDialog(true))
            }
        )
    }

    if (state.showLogoutDialog) {
        LogoutConfirmDialog(
            onDismissRequest = {
                viewModel.sendIntent(ProfileIntent.ChangeShowLogoutDialog(false))
            },
            onClickConfirm = {
                viewModel.sendIntent(ProfileIntent.ChangeShowLogoutDialog(false))
                viewModel.sendIntent(ProfileIntent.Logout)
            }
        )
    }
}
