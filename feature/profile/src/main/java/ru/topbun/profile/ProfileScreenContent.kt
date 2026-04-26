package ru.topbun.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import cafe.adriel.voyager.core.registry.ScreenRegistry
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf
import ru.topbun.core.ui.theme.Colors
import ru.topbun.navigation.ProfileScreenProvider
import ru.topbun.navigation.RecipeScreenProvider
import ru.topbun.profile.components.ProfileContent

data class ProfileScreenContent(
    private val userId: Int,
) : Screen {

    @Composable
    override fun Content() {
        val viewModel: ProfileViewModel = koinViewModel { parametersOf(ProfileState.Mode.Other(userId)) }
        val navigator = LocalNavigator.currentOrThrow

        LaunchedEffect(Unit) {
            viewModel.sendIntent(ProfileIntent.CheckSession)
        }


        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Colors.BACKGROUND)
                .statusBarsPadding()
        ) {
            ProfileContent(
                viewModel = viewModel,
                showBack = true,
                onBack = { navigator.pop() },
                onNavigateToSettings = {
                    val screen = ScreenRegistry.get(ProfileScreenProvider.Settings)
                    navigator.push(screen)
                },
                onClickRecipe = { recipeId ->
                    val screen = ScreenRegistry.get(RecipeScreenProvider.Detail(recipeId))
                    navigator.push(screen)
                }
            )
        }
    }
}
