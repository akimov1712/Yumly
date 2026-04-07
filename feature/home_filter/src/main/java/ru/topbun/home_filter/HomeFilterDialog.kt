package ru.topbun.home_filter

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf
import ru.topbun.core.ui.components.BottomDialogWrapper
import ru.topbun.core.ui.components.Height
import ru.topbun.core.ui.theme.Colors
import ru.topbun.core.ui.theme.Typography
import ru.topbun.core.ui.utils.ObserveAsEvents
import ru.topbun.domain.entity.recipe.getRecipe.GetRecipeFilterEntity
import ru.topbun.home_filter.components.Buttons
import ru.topbun.home_filter.components.CaloriesSection
import ru.topbun.home_filter.components.CategorySection
import ru.topbun.home_filter.components.DifficultySection
import ru.topbun.home_filter.components.DurationSection

@Composable
fun HomeFilterDialog(
    filter: GetRecipeFilterEntity,
    onApplyFilters: (GetRecipeFilterEntity) -> Unit,
    onDismissRequest: () -> Unit,
) = BottomDialogWrapper(
    onDismissRequest = onDismissRequest,
    containerColor = Colors.BACKGROUND
){
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Colors.BACKGROUND)
            .padding(bottom = 8.dp)
    ) {
        Title()
        Height(10.dp)
        FilterContent(
            filter = filter,
            onApplyFilters = onApplyFilters
        )
    }
}

@Composable
internal fun FilterContent(filter: GetRecipeFilterEntity, onApplyFilters: (GetRecipeFilterEntity) -> Unit) {

    val viewModel: HomeFilterViewModel = koinViewModel{ parametersOf(filter) }
    val state by viewModel.state.collectAsState()

    LaunchedEffect(filter) {
        viewModel.sendIntent(HomeFilterIntent.InitFilters(filter))
    }

    ObserveAsEvents(viewModel.events) {
        when(it){
            is HomeFilterEvent.ApplyFilters -> onApplyFilters(it.filters)
        }
    }


    Column(
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState())
            .padding(top = 20.dp, bottom = 12.dp)
    ) {
        FilterIsland {
            CategorySection(
                isExpanded = state.isExpanded,
                categoryUiState = state.categoryUiState,
                sortedCategories = state.sortedCategories,
                selectedCategoriesIds = state.selectedCategoriesIds,
                sendIntent = { viewModel.sendIntent(it) }
            )
        }
        Height(16.dp)
        FilterIsland {
            DurationSection(
                durationFromProgress = state.durationFromProgress,
                durationProgress = state.durationProgress,
                sendIntent = { viewModel.sendIntent(it) }
            )
        }
        Height(16.dp)
        FilterIsland {
            CaloriesSection(
                minCaloriesFromProgress = state.minCaloriesFromProgress,
                maxCaloriesFromProgress = state.maxCaloriesFromProgress,
                minCaloriesProgress = state.minCaloriesProgress,
                maxCaloriesProgress = state.maxCaloriesProgress,
                sendIntent = { viewModel.sendIntent(it) }
            )
        }
        Height(16.dp)
        FilterIsland {
            DifficultySection(
                difficultyList = state.difficultyList,
                selectedDifficultyIndex = state.selectedDifficultyIndex,
                sendIntent = { viewModel.sendIntent(it) }
            )
        }
        Height(24.dp)
        FilterIsland {
            Buttons(
                onClickClear = { viewModel.sendIntent(HomeFilterIntent.ClickClear) },
                onClickDone = { viewModel.sendIntent(HomeFilterIntent.ClickDone) }
            )
        }
    }
}


@Composable
private fun Title() {
    Text(
        modifier = Modifier.fillMaxWidth(),
        text = "Добавить фильтр",
        color = Colors.MAIN_TEXT,
        style = Typography.H2,
        textAlign = TextAlign.Center
    )
}

@Composable
private fun FilterIsland(
    content: @Composable () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .background(
                color = Colors.WHITE,
                shape = RoundedCornerShape(28.dp)
            )
            .padding(vertical = 20.dp)
    ) {
        content()
    }
}


