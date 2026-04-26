package ru.topbun.core.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ru.topbun.core.ui.utils.getBottomBarPadding
import ru.topbun.domain.ScreenUiState
import ru.topbun.domain.entity.recipe.RecipeEntity

@Composable
fun ColumnScope.RecipeList(
    recipes: List<RecipeEntity>,
    status: ScreenUiState,
    state: LazyListState,
    isEndList: Boolean,
    onRefresh: () -> Unit,
    onLoadMore: () -> Unit,
    onClickRecipe: ((RecipeEntity) -> Unit)? = null,
) {
    AppPullRefresh(
        modifier = Modifier
            .fillMaxWidth()
            .weight(1f),
        onRefresh = onRefresh
    ) {
        PaginationList(
            items = recipes,
            modifier = Modifier.fillMaxSize(),
            status = status,
            isEndList = isEndList,
            onLoadMore = onLoadMore,
            verticalArrangement = Arrangement.spacedBy(20.dp),
            contentPadding = PaddingValues(
                start = 12.dp,
                end = 12.dp,
                top = 15.dp,
                bottom = getBottomBarPadding()
            ),
            state = state,
            shimmerContent = { items(6) { RecipeShimmer() } },
            content = {
                itemsIndexed(
                    items = it,
                    key = {index, item ->
                        "id:${item.id} index:$index"
                    }
                ) { _, item ->
                    RecipeItem(item, onClick = onClickRecipe)
                }
            },
        )
    }
}


