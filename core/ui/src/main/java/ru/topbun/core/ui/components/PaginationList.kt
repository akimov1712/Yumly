package ru.topbun.core.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ru.topbun.core.ui.theme.Colors
import ru.topbun.domain.ScreenUiState

@Composable
fun <T>PaginationList(
    items: List<T>,
    status: ScreenUiState,
    isEndList: Boolean,
    modifier: Modifier = Modifier,
    state: LazyListState = rememberLazyListState(),
    verticalArrangement: Arrangement.Vertical = Arrangement.Top,
    horizontalAlignment: Alignment.Horizontal = Alignment.Start,
    contentPadding: PaddingValues = PaddingValues(0.dp),
    onLoadMore: () -> Unit,
    shimmerContent: LazyListScope.() -> Unit = {},
    emptyContent: (LazyListScope.() -> Unit)? = null,
    content: LazyListScope.(items: List<T>) -> Unit,
) {
    PreloadTrigger(
        state = state,
        status = status,
        isEndList = isEndList,
        onLoadMore = onLoadMore
    )

    LazyColumn(
        state = state,
        modifier = modifier,
        userScrollEnabled = items.isNotEmpty() || !status.isLoading,
        verticalArrangement = verticalArrangement,
        horizontalAlignment = horizontalAlignment,
        contentPadding = contentPadding
    ) {
        if (items.isEmpty() && status.isLoading) {
            shimmerContent()
        } else {
            content(items)
        }

        ListFooter(
            status = status,
            isEndList = isEndList,
            isEmpty = items.isEmpty(),
            emptyContent = emptyContent,
            onLoadMore = onLoadMore
        )
    }
}


private fun LazyListScope.ListFooter(
    status: ScreenUiState,
    isEndList: Boolean,
    isEmpty: Boolean,
    emptyContent: (LazyListScope.() -> Unit)?,
    onLoadMore: () -> Unit
) {

    when (status) {
        ScreenUiState.Error -> item {
            if (isEmpty) {
                FooterContentErrorBlock(onLoadMore)
            } else {
                FooterContentErrorInline(onLoadMore)
            }
        }
        ScreenUiState.Loading -> if (!isEndList) item {
            FooterContentLoading()
        }
        ScreenUiState.Success -> if (isEmpty) {
            if (emptyContent != null) emptyContent() else item { FooterContentEmptyList() }
        }

        else -> Unit
    }


}

@Composable
private fun FooterContentEmptyList() {
    ListEmptyBlock()
}

@Composable
private fun FooterContentLoading() {
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

@Composable
private fun FooterContentErrorBlock(onLoadMore: () -> Unit) {
    ListErrorBlock(onClickRetry = onLoadMore)
}

@Composable
private fun FooterContentErrorInline(onLoadMore: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp),
        contentAlignment = Alignment.Center
    ) {
        AppButton(
            text = "Загрузить снова",
        ) {
            onLoadMore()
        }
    }
}


@Composable
private fun PreloadTrigger(
    state: LazyListState,
    status: ScreenUiState,
    isEndList: Boolean,
    onLoadMore: () -> Unit
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