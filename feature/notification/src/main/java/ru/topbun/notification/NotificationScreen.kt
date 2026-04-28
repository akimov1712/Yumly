package ru.topbun.notification

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import cafe.adriel.voyager.core.registry.ScreenRegistry
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabOptions
import org.koin.compose.viewmodel.koinViewModel
import ru.topbun.core.ui.R
import ru.topbun.core.ui.components.UnauthorizedSection
import ru.topbun.core.ui.theme.Colors
import ru.topbun.core.ui.utils.ObserveAsEvents
import ru.topbun.navigation.ProfileScreenProvider
import ru.topbun.navigation.RecipeScreenProvider
import ru.topbun.navigation.auth.AuthScreenProvider
import ru.topbun.notification.NotificationState.NotificationUiState.NEED_AUTH
import ru.topbun.notification.NotificationState.NotificationUiState.SUCCESS
import ru.topbun.notification.components.Header
import ru.topbun.notification.components.NotificationList

object NotificationScreen : Tab {

    override val options @Composable get() = TabOptions(
        index = 3U,
        title = "Уведомления",
        icon = painterResource(R.drawable.ic_tabs_notification)
    )

    @Composable
    override fun Content() {
        val viewModel: NotificationViewModel = koinViewModel()
        val state by viewModel.state.collectAsState()
        val navigator = LocalNavigator.currentOrThrow.parent

        LaunchedEffect(Unit) {
            viewModel.sendIntent(NotificationIntent.CheckSession)
        }

        ObserveAsEvents(viewModel.events) { event ->
            when (event) {
                is NotificationEvent.NavigateToProfile -> {
                    val screen = ScreenRegistry.get(ProfileScreenProvider.User(event.userId))
                    navigator?.push(screen)
                }
                is NotificationEvent.NavigateToRecipe -> {
                    val screen = ScreenRegistry.get(RecipeScreenProvider.Detail(event.recipeId))
                    navigator?.push(screen)
                }
            }
        }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Colors.BACKGROUND)
                .statusBarsPadding()
        ) {
            when (state.notificationUiState) {
                SUCCESS -> NotificationContent()
                NEED_AUTH -> UnauthorizedSection {
                    val screen = ScreenRegistry.get(AuthScreenProvider.Login)
                    navigator?.push(screen)
                }
                else -> Unit
            }
        }
    }
}

@Composable
private fun NotificationContent(
    viewModel: NotificationViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsState()

    Column(modifier = Modifier.fillMaxSize()) {
        NotificationList(
            state = state.listState,
            status = state.list.status,
            isEndList = state.list.isEndList,
            groups = state.groups,
            onLoadMore = { viewModel.sendIntent(NotificationIntent.LoadNotifications) },
            onRefresh = { viewModel.sendIntent(NotificationIntent.RefreshNotifications) },
            onClickInitiator = { userId ->
                viewModel.sendIntent(NotificationIntent.ClickInitiator(userId))
            },
            onClickRecipe = { recipeId, authorUserId ->
                viewModel.sendIntent(NotificationIntent.ClickRecipe(recipeId, authorUserId))
            },
            headerContent = { Header() }
        )
    }
}
