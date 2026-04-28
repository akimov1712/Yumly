package ru.topbun.profile

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import cafe.adriel.voyager.core.registry.ScreenRegistry
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabOptions
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf
import ru.topbun.core.ui.R
import ru.topbun.core.ui.components.UnauthorizedSection
import ru.topbun.core.ui.theme.Colors
import ru.topbun.core.ui.utils.ObserveAsEvents
import ru.topbun.navigation.BmiScreenProvider
import ru.topbun.navigation.ProfileScreenProvider
import ru.topbun.navigation.RecipeScreenProvider
import ru.topbun.navigation.RootScreenProvider
import ru.topbun.navigation.auth.AuthScreenProvider
import ru.topbun.navigation.auth.AuthStartScreen
import ru.topbun.navigation.utills.root
import ru.topbun.profile.ProfileState.ProfileUiState.NEED_AUTH
import ru.topbun.profile.ProfileState.ProfileUiState.SUCCESS
import ru.topbun.profile.components.ProfileContent

private const val PRIVACY_POLICY_URL = "https://yumly.app/privacy"
private const val USER_AGREEMENT_URL = "https://yumly.app/terms"

object ProfileScreen : Tab {

    override val options @Composable get() = TabOptions(
        index = 4U,
        title = "Profile",
        icon = painterResource(R.drawable.ic_tab_profile)
    )

    @Composable
    override fun Content() {
        val context = LocalContext.current
        val viewModel: ProfileViewModel = koinViewModel { parametersOf(ProfileState.Mode.Self) }
        val state by viewModel.state.collectAsState()
        val navigator = LocalNavigator.currentOrThrow.parent

        LaunchedEffect(Unit) {
            viewModel.sendIntent(ProfileIntent.CheckSession)
        }

        ObserveAsEvents(viewModel.events) { event ->
            when (event) {
                ProfileEvent.LoggedOut -> {
                    val authScreen = ScreenRegistry.get(
                        RootScreenProvider.Auth(AuthStartScreen.LOGIN)
                    )
                    navigator?.root()?.replaceAll(authScreen)
                }
                ProfileEvent.NavigateToAuth -> {
                    val screen = ScreenRegistry.get(AuthScreenProvider.Login)
                    navigator?.push(screen)
                }
                ProfileEvent.NavigateToBmi -> {
                    val screen = ScreenRegistry.get(BmiScreenProvider.Main)
                    navigator?.push(screen)
                }
                is ProfileEvent.OpenUrl -> openUrl(context, event.url)
            }
        }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Colors.BACKGROUND)
                .statusBarsPadding()
        ) {
            when (state.profileUiState) {
                SUCCESS -> ProfileContent(
                    viewModel = viewModel,
                    showBack = false,
                    onBack = {},
                    onNavigateToSettings = {
                        val screen = ScreenRegistry.get(ProfileScreenProvider.Settings)
                        navigator?.push(screen)
                    },
                    onClickRecipe = { recipeId ->
                        val screen = ScreenRegistry.get(RecipeScreenProvider.Detail(recipeId))
                        navigator?.push(screen)
                    },
                    onClickFollowers = { userId ->
                        val screen = ScreenRegistry.get(
                            ProfileScreenProvider.Follows(
                                userId = userId,
                                initialTab = ProfileScreenProvider.FollowsTab.Followers
                            )
                        )
                        navigator?.push(screen)
                    },
                    onClickFollowing = { userId ->
                        val screen = ScreenRegistry.get(
                            ProfileScreenProvider.Follows(
                                userId = userId,
                                initialTab = ProfileScreenProvider.FollowsTab.Following
                            )
                        )
                        navigator?.push(screen)
                    },
                    onClickBmi = {
                        val screen = ScreenRegistry.get(BmiScreenProvider.Main)
                        navigator?.push(screen)
                    },
                    onClickPrivacyPolicy = {
                        openUrl(context, PRIVACY_POLICY_URL)
                    },
                    onClickUserAgreement = {
                        openUrl(context, USER_AGREEMENT_URL)
                    }
                )
                NEED_AUTH -> UnauthorizedSection {
                    val screen = ScreenRegistry.get(AuthScreenProvider.Login)
                    navigator?.push(screen)
                }
                else -> Unit
            }
        }
    }
}

private fun openUrl(context: Context, url: String) {
    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    runCatching { context.startActivity(intent) }
}
