package ru.topbun.recipe

import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.registry.ScreenRegistry
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.core.screen.ScreenKey
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import org.koin.compose.koinInject
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf
import ru.topbun.core.android.SnackbarManager
import ru.topbun.core.ui.R
import ru.topbun.core.ui.components.AppPullRefresh
import ru.topbun.core.ui.components.ListErrorBlock
import ru.topbun.core.ui.theme.Colors
import ru.topbun.core.ui.utils.ObserveAsEvents
import ru.topbun.navigation.ProfileScreenProvider.User
import ru.topbun.recipe.components.DeleteRecipeDialog
import ru.topbun.recipe.components.DescriptionSection
import ru.topbun.recipe.components.HeroSection
import ru.topbun.recipe.components.IngredientsSection
import ru.topbun.recipe.components.NutritionCard
import ru.topbun.recipe.components.OfflineCacheBanner
import ru.topbun.recipe.components.QuickStatsRow
import ru.topbun.recipe.components.RecipeShimmerScreen
import ru.topbun.recipe.components.StepsSection
import ru.topbun.recipe.components.TimerSection
import ru.topbun.recipe.components.TopBar

data class RecipeScreen(
    private val recipeId: Int,
    private val fromCache: Boolean
) : Screen {

    override val key: ScreenKey
        get() = recipeId.toString()

    @Composable
    override fun Content() {
        val context = LocalContext.current
        val snackbarManager = koinInject<SnackbarManager>()
        val viewModel: RecipeViewModel = koinViewModel { parametersOf(recipeId, fromCache) }
        val state by viewModel.state.collectAsState()
        val navigator = LocalNavigator.currentOrThrow

        val view = LocalView.current

        DisposableEffect(Unit) {
            view.keepScreenOn = true
            onDispose {
                view.keepScreenOn = false
            }
        }

        LaunchedEffect(Unit) {
            viewModel.sendIntent(RecipeIntent.LoadRecipe)
        }

        ObserveAsEvents(viewModel.events) { event ->
            when (event) {
                is RecipeEvent.NavigateToProfile -> {
                    val screen = ScreenRegistry.get(User(event.userId))
                    navigator.push(screen)
                }
                is RecipeEvent.Share -> {
                    val intent = Intent(Intent.ACTION_SEND).apply {
                        type = "text/plain"
                        putExtra(Intent.EXTRA_TEXT, event.text)
                    }

                    context.startActivity(Intent.createChooser(intent, "Поделиться рецептом"))
                }
                RecipeEvent.RecipeDeleted -> navigator.pop()
                RecipeEvent.TimerFinished -> showNotifyEndOfTimer(context, snackbarManager)
            }
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

        if (state.showDeleteDialog) {
            DeleteRecipeDialog(
                isLoading = state.deleteLoading,
                onDismissRequest = { viewModel.sendIntent(RecipeIntent.ChangeShowDeleteDialog(false)) },
                onClickConfirm = { viewModel.sendIntent(RecipeIntent.DeleteRecipe) }
            )
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
    val hasDescription = !recipe.description.isNullOrBlank() || recipe.tags.isNotEmpty()

    AppPullRefresh(
        modifier = Modifier.fillMaxSize(),
        onRefresh = { onIntent(RecipeIntent.Refresh) }
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            state = listState,
            contentPadding = PaddingValues(bottom = 24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item("hero") {
                HeroSection(
                    recipe = recipe,
                    isOwnRecipe = state.isOwnRecipe,
                    onClickAuthor = { onIntent(RecipeIntent.ClickAuthor) }
                )
            }
            if (state.fromCache){
                item("offline cache banner") {
                    OfflineCacheBanner()
                }
            }
            item("stats") {
                QuickStatsRow(
                    recipe = recipe,
                    modifier = Modifier.padding(horizontal = 12.dp)
                )
            }
            if (hasDescription) {
                item("description") {
                    DescriptionSection(
                        recipe = recipe,
                        modifier = Modifier.padding(horizontal = 12.dp)
                    )
                }
            }
            item("nutrition") {
                NutritionCard(
                    recipe = recipe,
                    modifier = Modifier.padding(horizontal = 12.dp)
                )
            }
            item("timer") {
                TimerSection(
                    timer = state.timer,
                    onChangeMode = { onIntent(RecipeIntent.ChangeTimerMode(it)) },
                    onChangeTarget = { onIntent(RecipeIntent.ChangeTimerTarget(it)) },
                    onStart = { onIntent(RecipeIntent.StartTimer) },
                    onPause = { onIntent(RecipeIntent.PauseTimer) },
                    onReset = { onIntent(RecipeIntent.ResetTimer) },
                    modifier = Modifier.padding(horizontal = 12.dp)
                )
            }
            item("ingredients") {
                IngredientsSection(
                    ingredients = recipe.ingredients,
                    checkedIndices = state.checkedIngredients,
                    progress = state.ingredientProgress,
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
        Box(
            modifier = Modifier.systemBarsPadding()
                .padding(12.dp)
        ){
            TopBar(
                isOwnRecipe = state.isOwnRecipe,
                isFavorite = state.isFavorite,
                favoriteLoading = state.favoriteLoading,
                onClickBack = onBack,
                onClickShare = { onIntent(RecipeIntent.ClickShare) },
                onClickFavorite = { onIntent(RecipeIntent.ToggleFavorite) },
                onClickDelete = { onIntent(RecipeIntent.ChangeShowDeleteDialog(true)) },
            )
        }
    }
}

private fun showNotifyEndOfTimer(context: Context, snackbarManager: SnackbarManager) {
    val mediaPlayer = MediaPlayer.create(context, R.raw.sound_timer_finished)
    mediaPlayer.start()
    snackbarManager.showMessage("Готово! Таймер завершён")
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
                .padding(horizontal = 12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            ListErrorBlock(
                title = "Не удалось загрузить рецепт",
                message = "Проверьте подключение и попробуйте ещё раз",
                onClickRetry = onClickRetry
            )
        }
    }
}
