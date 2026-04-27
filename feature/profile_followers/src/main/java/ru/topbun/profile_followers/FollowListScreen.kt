package ru.topbun.profile_followers

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.registry.ScreenRegistry
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf
import ru.topbun.core.ui.components.AppPullRefresh
import ru.topbun.core.ui.components.Height
import ru.topbun.core.ui.components.PaginationList
import ru.topbun.core.ui.theme.Colors
import ru.topbun.core.ui.utils.getBottomBarPadding
import ru.topbun.navigation.ProfileScreenProvider
import ru.topbun.profile_followers.components.EmptyContent
import ru.topbun.profile_followers.components.FollowTabsBar
import ru.topbun.profile_followers.components.Header
import ru.topbun.profile_followers.components.UserItem
import ru.topbun.profile_followers.components.UserShimmer

data class FollowListScreen(
    private val userId: Int,
    private val initialTab: ProfileScreenProvider.FollowsTab,
) : Screen {

    @Composable
    override fun Content() {
        val viewModel: FollowListViewModel = koinViewModel { parametersOf(userId, initialTab) }
        val state by viewModel.state.collectAsState()
        val navigator = LocalNavigator.currentOrThrow

        LaunchedEffect(Unit) {
            viewModel.sendIntent(FollowListIntent.LoadProfile)
        }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Colors.BACKGROUND)
                .statusBarsPadding()
        ) {
            Column(modifier = Modifier.fillMaxSize()) {
                Header(
                    profile = state.targetProfile,
                    onClickBack = { navigator.pop() }
                )
                Height(12.dp)
                FollowTabsBar(
                    selectedTab = state.selectedTab,
                    followersCount = state.targetProfile?.countFollowers ?: 0,
                    followingCount = state.targetProfile?.countFollowing ?: 0,
                    onSelect = { viewModel.sendIntent(FollowListIntent.ChangeTab(it)) }
                )

                AppPullRefresh(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    onRefresh = { viewModel.sendIntent(FollowListIntent.Refresh) }
                ) {
                    PaginationList(
                        items = state.visibleList.users,
                        status = state.visibleList.status,
                        isEndList = state.visibleList.isEndList,
                        state = state.visibleListState,
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                        contentPadding = PaddingValues(
                            start = 12.dp,
                            end = 12.dp,
                            top = 16.dp,
                            bottom = 16.dp + getBottomBarPadding()
                        ),
                        onLoadMore = {
                            val intent = when (state.selectedTab) {
                                ProfileScreenProvider.FollowsTab.Followers -> FollowListIntent.LoadFollowers
                                ProfileScreenProvider.FollowsTab.Following -> FollowListIntent.LoadFollowing
                            }
                            viewModel.sendIntent(intent)
                        },
                        shimmerContent = {
                            items(8) { UserShimmer() }
                        },
                        emptyContent = {
                            item("empty") {
                                EmptyContent(
                                    title = when (state.selectedTab) {
                                        ProfileScreenProvider.FollowsTab.Followers ->
                                            "Подписчиков пока нет"
                                        ProfileScreenProvider.FollowsTab.Following ->
                                            "Пока ни на кого не подписан"
                                    },
                                    description = when (state.selectedTab) {
                                        ProfileScreenProvider.FollowsTab.Followers ->
                                            "Здесь будут все, кто подписан на этот профиль"
                                        ProfileScreenProvider.FollowsTab.Following ->
                                            "Здесь появятся пользователи, на которых подписан профиль"
                                    }
                                )
                            }
                        },
                        content = { users ->
                            itemsIndexed(
                                items = users,
                                key = { index, item -> "id:${item.userId} index:$index" }
                            ) { _, item ->
                                UserItem(
                                    profile = item,
                                    onClick = {
                                        val screen = ScreenRegistry.get(
                                            ProfileScreenProvider.User(item.userId)
                                        )
                                        navigator.push(screen)
                                    }
                                )
                            }
                        }
                    )
                }
            }
        }
    }
}
