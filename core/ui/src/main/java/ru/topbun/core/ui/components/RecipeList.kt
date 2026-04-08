package ru.topbun.core.ui.components

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImagePainter
import com.valentinilk.shimmer.shimmer
import ru.topbun.core.ui.R
import ru.topbun.core.ui.theme.Colors
import ru.topbun.core.ui.theme.Typography
import ru.topbun.core.ui.utils.LocalBottomBarPadding
import ru.topbun.core.ui.utils.formatCookingTime
import ru.topbun.core.ui.utils.formatIngredientCount
import ru.topbun.core.ui.utils.formatStepCount
import ru.topbun.domain.ScreenUiState
import ru.topbun.domain.entity.recipe.RecipeEntity

@Composable
fun ColumnScope.RecipeList(
    recipes: List<RecipeEntity>,
    status: ScreenUiState,
    state: LazyListState,
    isEndList: Boolean,
    onRefresh: () -> Unit,
    onLoadMore: () -> Unit
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
                bottom = LocalBottomBarPadding.current
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
                    RecipeItem(item)
                }
            },
        )
    }
}


@Composable
private fun RecipeItem(recipe: RecipeEntity) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(32.dp))
            .background(Colors.WHITE)
            .padding(8.dp),
        horizontalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        Preview(recipe.smallImage)
        Information(recipe)
    }
}

@Composable
private fun Information(recipe: RecipeEntity) {
    Column(
        Modifier.padding(vertical = 6.dp)
    ) {
        Title(recipe)
        Height(10.dp)
        ChipList(recipe)
    }
}

@Composable
private fun Title(recipe: RecipeEntity) {
    Text(
        text = recipe.title,
        style = Typography.H2,
        lineHeight = 22.sp,
        color = Colors.BLUE_TEXT
    )
}

@Composable
private fun ChipList(recipe: RecipeEntity) {
    Column(
        verticalArrangement = Arrangement.spacedBy(5.dp)
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Chip(
                icon = painterResource(R.drawable.ic_calories),
                title = "${recipe.kcal} ккал"
            )
            Box(
                Modifier
                    .size(4.dp)
                    .background(Colors.SECONDARY_TEXT, CircleShape)
            )
            Chip(
                icon = painterResource(R.drawable.ic_time),
                title = formatCookingTime(recipe.cookingTime)
            )
        }
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Chip(
                icon = painterResource(R.drawable.ic_ingredients),
                title = formatIngredientCount(recipe.ingredients.size)
            )
            Box(
                Modifier
                    .size(4.dp)
                    .background(Colors.SECONDARY_TEXT, CircleShape)
            )
            Chip(
                icon = painterResource(R.drawable.ic_steps),
                title = formatStepCount(recipe.steps.size)
            )
        }
    }
}

@Composable
private fun Chip(
    icon: Painter,
    title: String
) {
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painter = icon,
            modifier = Modifier.size(12.dp),
            contentDescription = title,
            tint = Colors.SECONDARY_TEXT
        )
        Width(4.dp)
        Text(
            text = title,
            color = Colors.SECONDARY_TEXT,
            style = Typography.S
        )
    }
}

@Composable
private fun Preview(url: String?) {
    var isLoad by remember { mutableStateOf(true) }
    val loaderModifier = if (isLoad) Modifier.shimmer() else Modifier

    Box(
        modifier = Modifier.size(100.dp),
        contentAlignment = Alignment.Center
    ){
        AppAsyncImage(
            url = url,
            modifier = Modifier
                .fillMaxSize()
                .clip(RoundedCornerShape(24.dp))
                .then(loaderModifier)
                .background(Colors.FORM),
            contentScale = ContentScale.Crop,
            onState = {
                isLoad = it !is AsyncImagePainter.State.Success
                if (it is AsyncImagePainter.State.Error) {
                    Log.e("Async Image", it.result.throwable.message ?: "???")
                }
            },
        )
        if (url.isNullOrEmpty()){
            Icon(
                modifier = Modifier.fillMaxSize().padding(16.dp),
                painter = painterResource(R.drawable.ic_recipe_preview_placeholder),
                contentDescription = null,
                tint = Colors.SECONDARY_TEXT
            )
        }
    }

}

