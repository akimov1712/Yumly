package ru.topbun.profile.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ru.topbun.core.ui.components.AppButton
import ru.topbun.core.ui.components.AppPullRefresh
import ru.topbun.core.ui.components.RecipeItem
import ru.topbun.core.ui.components.RecipeShimmer
import ru.topbun.core.ui.theme.Colors
import ru.topbun.core.ui.utils.getBottomBarPadding
import ru.topbun.domain.ScreenUiState
import ru.topbun.domain.entity.recipe.RecipeEntity

@Composable
internal fun ProfileRecipeList(
    state: LazyListState,
    listState: ScreenUiState,
    isEndList: Boolean,
    recipes: List<RecipeEntity>,
    emptyTitle: String,
    emptyDescription: String,
    headerContent: @Composable () -> Unit,
    tabsContent: @Composable () -> Unit,
    onRefresh: () -> Unit,
    onLoadMore: () -> Unit,
    onClickRecipe: ((RecipeEntity) -> Unit)? = null,
) {
    PreloadTrigger(
        state = state,
        status = listState,
        isEndList = isEndList,
        onLoadMore = onLoadMore
    )

    AppPullRefresh(
        modifier = Modifier.fillMaxSize(),
        onRefresh = onRefresh
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            state = state,
            contentPadding = PaddingValues(
                start = 12.dp,
                end = 12.dp,
                bottom = getBottomBarPadding()
            ),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            item("profile_header") { headerContent() }
            item("profile_tabs") { tabsContent() }

            when {
                recipes.isEmpty() && listState.isLoading -> {
                    items(3) { _ -> RecipeShimmer() }
                }
                recipes.isEmpty() && listState.isSuccess -> item("profile_empty") {
                    EmptyContent(
                        title = emptyTitle,
                        description = emptyDescription
                    )
                }
                recipes.isEmpty() && listState.isError -> item("profile_error") {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 24.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        AppButton(
                            text = "Загрузить снова",
                            onClick = onLoadMore
                        )
                    }
                }
                else -> itemsIndexed(
                    items = recipes,
                    key = { index, item -> "id:${item.id} index:$index" }
                ) { _, item ->
                    RecipeItem(item, onClick = onClickRecipe)
                }
            }

            when {
                listState.isLoading && recipes.isNotEmpty() && !isEndList -> item("loading_footer") {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(24.dp),
                            strokeWidth = 2.5.dp,
                            trackColor = Colors.PRIMARY
                        )
                    }
                }
                listState.isError && recipes.isNotEmpty() -> item("error_footer") {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        AppButton(
                            text = "Загрузить снова",
                            onClick = onLoadMore
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun PreloadTrigger(
    state: LazyListState,
    status: ScreenUiState,
    isEndList: Boolean,
    onLoadMore: () -> Unit,
) {
    val shouldLoadMore = remember(state) {
        derivedStateOf {
            val layoutInfo = state.layoutInfo
            val lastVisibleItemIndex =
                layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: return@derivedStateOf false
            val totalItemsCount = layoutInfo.totalItemsCount
            lastVisibleItemIndex >= totalItemsCount - 3
        }
    }

    LaunchedEffect(shouldLoadMore.value) {
        if (
            shouldLoadMore.value &&
            !status.isLoading &&
            !status.isError &&
            !isEndList
        ) {
            onLoadMore()
        }
    }
}
