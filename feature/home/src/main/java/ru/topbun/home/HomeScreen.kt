package ru.topbun.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabOptions
import org.koin.compose.viewmodel.koinViewModel
import ru.topbun.core.ui.R
import ru.topbun.core.ui.components.Height
import ru.topbun.core.ui.components.RecipeList
import ru.topbun.core.ui.theme.Colors
import ru.topbun.home.components.Header
import ru.topbun.home.components.SearchTypeBar
import ru.topbun.home_filter.HomeFilterDialog

object HomeScreen: Tab {

    override val options @Composable get() = TabOptions(
        index = 0U,
        title = "Home",
        icon = painterResource(R.drawable.ic_tabs_home)
    )

    @Composable
    override fun Content() {
        val viewModel: HomeViewModel = koinViewModel()
        val state by viewModel.state.collectAsState()

        Column(
            modifier = Modifier.fillMaxSize()
                .background(Colors.BACKGROUND)
                .statusBarsPadding()
        ) {
            Header(
                text = state.search,
                isFilterChanged = state.isFilterChanged,
                onClickFilter = { viewModel.sendIntent(HomeIntent.ChangeShowFilterDialog(true)) }
            ) { viewModel.sendIntent(HomeIntent.ChangeSearch(it)) }
            Height(10.dp)
            SearchTypeBar(
                types = state.searchTypeList,
                selectedIndex = state.selectedSearchTypeIndex,
                isVisible = state.searchTypeVisible
            ) { viewModel.sendIntent(HomeIntent.ChangeSearchType(it)) }
            Height(5.dp)
            RecipeList(
                recipes = state.recipes,
                state = state.recipeListState
            )
        }

        if (state.showFilterDialog){
            HomeFilterDialog(
                filter = state.recipeFilters,
                onApplyFilters = {
                    viewModel.sendIntent(HomeIntent.ChangeRecipeFilter(it))
                    viewModel.sendIntent(HomeIntent.ChangeShowFilterDialog(false))
                },
                onDismissRequest = { viewModel.sendIntent(HomeIntent.ChangeShowFilterDialog(false)) }
            )
        }

    }


}