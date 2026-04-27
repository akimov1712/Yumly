package ru.topbun.profile.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ru.topbun.core.ui.components.AppPullRefresh
import ru.topbun.core.ui.components.PaginationList
import ru.topbun.core.ui.components.RecipeItem
import ru.topbun.core.ui.components.RecipeShimmer
import ru.topbun.core.ui.utils.getBottomBarPadding
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
    onClickRecipe: (recipeId: Int) -> Unit = {},
    onClickFollowers: (userId: Int) -> Unit = {},
    onClickFollowing: (userId: Int) -> Unit = {},
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
                val headerItems: LazyListScope.() -> Unit = {
                    item("profile_info") {
                        val profileUserId = state.profile?.userId
                        ProfileInfo(
                            profile = state.profile,
                            isSelf = state.isSelf,
                            followLoading = state.followLoading,
                            onClickFollow = { viewModel.sendIntent(ProfileIntent.SwitchFollow) },
                            onClickFollowers = {
                                profileUserId?.let { onClickFollowers(it) }
                            },
                            onClickFollowing = {
                                profileUserId?.let { onClickFollowing(it) }
                            }
                        )
                    }
                    item("profile_tabs") {
                        ProfileTabsBar(
                            selectedTab = state.selectedTab,
                            onSelect = { viewModel.sendIntent(ProfileIntent.ChangeTab(it)) }
                        )
                    }
                }

                AppPullRefresh(
                    modifier = Modifier.fillMaxWidth().weight(1f),
                    onRefresh = { viewModel.sendIntent(ProfileIntent.Refresh) }
                ) {
                    PaginationList(
                        items = state.visibleList.recipes,
                        status = state.visibleList.status,
                        isEndList = state.visibleList.isEndList,
                        state = state.visibleListState,
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.spacedBy(20.dp),
                        contentPadding = PaddingValues(
                            start = 12.dp,
                            end = 12.dp,
                            bottom = getBottomBarPadding()
                        ),
                        onLoadMore = {
                            val intent = if (state.selectedTab == ProfileState.ProfileTab.MyRecipes) {
                                ProfileIntent.LoadRecipes
                            } else {
                                ProfileIntent.LoadLiked
                            }
                            viewModel.sendIntent(intent)
                        },
                        shimmerContent = {
                            headerItems()
                            items(3) { RecipeShimmer() }
                        },
                        content = { recipes ->
                            headerItems()
                            itemsIndexed(
                                items = recipes,
                                key = { index, item -> "id:${item.id} index:$index" }
                            ) { _, item ->
                                RecipeItem(item, onClick = { onClickRecipe(it.id) })
                            }
                        }
                    )
                }
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
