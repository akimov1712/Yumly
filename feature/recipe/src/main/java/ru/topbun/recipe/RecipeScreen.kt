package ru.topbun.recipe

import android.content.Intent
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.registry.ScreenRegistry
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import org.koin.compose.koinInject
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf
import ru.topbun.core.android.SnackbarManager
import ru.topbun.core.ui.R
import ru.topbun.core.ui.components.AppButton
import ru.topbun.core.ui.components.AppPullRefresh
import ru.topbun.core.ui.theme.Colors
import ru.topbun.core.ui.theme.Typography
import ru.topbun.core.ui.utils.ObserveAsEvents
import ru.topbun.navigation.ProfileScreenProvider
import ru.topbun.recipe.components.CookCtaButton
import ru.topbun.recipe.components.CookingTimerCard
import ru.topbun.recipe.components.DescriptionSection
import ru.topbun.recipe.components.HeroSection
import ru.topbun.recipe.components.IngredientsSection
import ru.topbun.recipe.components.NutritionCard
import ru.topbun.recipe.components.QuickStatsRow
import ru.topbun.recipe.components.RecipeShimmerScreen
import ru.topbun.recipe.components.StepsSection

data class RecipeScreen(
    private val recipeId: Int,
) : Screen {

    @Composable
    override fun Content() {
        val viewModel: RecipeViewModel = koinViewModel { parametersOf(recipeId) }
        val state by viewModel.state.collectAsState()
        val navigator = LocalNavigator.currentOrThrow
        val context = LocalContext.current
        val snackbarManager = koinInject<SnackbarManager>()

        LaunchedEffect(Unit) {
            viewModel.sendIntent(RecipeIntent.LoadRecipe)
        }

        ObserveAsEvents(viewModel.events) { event ->
            when (event) {
                is RecipeEvent.NavigateToProfile -> {
                    val screen = ScreenRegistry.get(ProfileScreenProvider.User(event.userId))
                    navigator.push(screen)
                }
                is RecipeEvent.Share -> {
                    val intent = Intent(Intent.ACTION_SEND).apply {
                        type = "text/plain"
                        putExtra(Intent.EXTRA_TEXT, event.text)
                    }
                    context.startActivity(Intent.createChooser(intent, "Поделиться рецептом"))
                }
                RecipeEvent.CookingDone -> {
                    snackbarManager.showMessage("Готово! Время приготовления вышло")
                }
            }
        }

        if (state.isCookingMode) {
            BackHandler { viewModel.sendIntent(RecipeIntent.StopCooking) }
        }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Colors.BACKGROUND)
        ) {
            when {
                state.recipe == null && state.recipeStatus.isLoading -> RecipeShimmerScreen()
                state.recipe == null && state.recipeStatus.isError -> ErrorState(
                    onClickRetry = { viewModel.sendIntent(RecipeIntent.LoadRecipe) },
                    onClickBack = { navigator.pop() }
                )
                state.recipe != null -> RecipeContent(
                    state = state,
                    onIntent = viewModel::sendIntent,
                    onBack = { navigator.pop() }
                )
                else -> Unit
            }
        }
    }
}

@Composable
private fun RecipeContent(
    state: RecipeState,
    onIntent: (RecipeIntent) -> Unit,
    onBack: () -> Unit,
) {
    val recipe = state.recipe ?: return
    val listState = rememberLazyListState()

    Box(modifier = Modifier.fillMaxSize()) {
        AppPullRefresh(
            modifier = Modifier.fillMaxSize(),
            onRefresh = { onIntent(RecipeIntent.Refresh) }
        ) {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                state = listState,
                contentPadding = PaddingValues(bottom = 120.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                item("hero") {
                    HeroSection(
                        recipe = recipe,
                        isFavorite = state.isFavorite,
                        favoriteLoading = state.favoriteLoading,
                        onClickBack = onBack,
                        onClickShare = { onIntent(RecipeIntent.ClickShare) },
                        onClickFavorite = { onIntent(RecipeIntent.ToggleFavorite) },
                        onClickAuthor = { onIntent(RecipeIntent.ClickAuthor) }
                    )
                }
                item("stats") {
                    QuickStatsRow(
                        recipe = recipe,
                        modifier = Modifier.padding(horizontal = 12.dp)
                    )
                }
                item("description") {
                    DescriptionSection(
                        recipe = recipe,
                        modifier = Modifier.padding(horizontal = 12.dp)
                    )
                }
                item("nutrition") {
                    NutritionCard(
                        recipe = recipe,
                        modifier = Modifier.padding(horizontal = 12.dp)
                    )
                }
                item("ingredients") {
                    IngredientsSection(
                        ingredients = recipe.ingredients,
                        mode = state.ingredientMode,
                        checkedIndices = state.checkedForCurrentMode,
                        progress = state.ingredientProgress,
                        onChangeMode = { onIntent(RecipeIntent.ChangeIngredientMode(it)) },
                        onToggleIngredient = { onIntent(RecipeIntent.ToggleIngredient(it)) },
                        onClickReset = { onIntent(RecipeIntent.ResetIngredients) },
                        modifier = Modifier.padding(horizontal = 12.dp)
                    )
                }
                item("steps") {
                    StepsSection(
                        steps = recipe.steps,
                        completedSteps = state.completedSteps,
                        progress = state.stepProgress,
                        onToggleStep = { onIntent(RecipeIntent.ToggleStep(it)) },
                        onClickReset = { onIntent(RecipeIntent.ResetSteps) },
                        modifier = Modifier.padding(horizontal = 12.dp)
                    )
                }
            }
        }

        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .padding(bottom = 24.dp)
        ) {
            if (state.isCookingMode) {
                CookingTimerCard(
                    secondsLeft = state.cookingTimerSecondsLeft,
                    isPaused = state.cookingTimerPaused,
                    progress = state.cookingTimerProgress,
                    onPauseToggle = {
                        if (state.cookingTimerPaused) onIntent(RecipeIntent.ResumeTimer)
                        else onIntent(RecipeIntent.PauseTimer)
                    },
                    onReset = { onIntent(RecipeIntent.ResetTimer) },
                    onStop = { onIntent(RecipeIntent.StopCooking) },
                    modifier = Modifier.padding(horizontal = 12.dp)
                )
            } else {
                CookCtaButton(
                    onClick = { onIntent(RecipeIntent.StartCooking) }
                )
            }
        }
    }
}

@Composable
private fun ErrorState(
    onClickRetry: () -> Unit,
    onClickBack: () -> Unit,
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding(),
    ) {
        IconButton(
            modifier = Modifier
                .padding(12.dp)
                .size(44.dp),
            onClick = onClickBack
        ) {
            Icon(
                modifier = Modifier.size(20.dp),
                painter = painterResource(R.drawable.ic_back),
                contentDescription = null,
                tint = Colors.MAIN_TEXT
            )
        }
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Не удалось загрузить рецепт",
                style = Typography.H2,
                color = Colors.MAIN_TEXT,
                textAlign = TextAlign.Center
            )
            Text(
                text = "Попробуйте ещё раз",
                style = Typography.P2,
                color = Colors.SECONDARY_TEXT,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(top = 8.dp)
            )
            AppButton(
                modifier = Modifier
                    .padding(top = 24.dp)
                    .fillMaxWidth(),
                text = "Загрузить снова",
                onClick = onClickRetry
            )
        }
    }
}
