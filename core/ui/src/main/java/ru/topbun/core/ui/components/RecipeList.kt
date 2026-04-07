package ru.topbun.core.ui.components

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
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import ru.topbun.core.ui.utils.formatCookingTime
import ru.topbun.core.ui.utils.formatIngredientCount
import ru.topbun.core.ui.utils.formatStepCount
import ru.topbun.core.ui.R
import ru.topbun.core.ui.theme.Colors
import ru.topbun.core.ui.theme.Typography
import ru.topbun.core.ui.utils.LocalBottomBarPadding
import ru.topbun.core.ui.utils.formatRecipeDifficulty
import ru.topbun.domain.entity.recipe.RecipeDifficulty.Easy
import ru.topbun.domain.entity.recipe.RecipeDifficulty.Hard
import ru.topbun.domain.entity.recipe.RecipeDifficulty.Normal
import ru.topbun.domain.entity.recipe.RecipeEntity

@Composable
fun ColumnScope.RecipeList(
    recipes: List<RecipeEntity>,
    state: LazyListState = remember { LazyListState() },
) {
    LazyColumn(
        state = state,
        modifier = Modifier
            .fillMaxWidth()
            .weight(1f),
        verticalArrangement = Arrangement.spacedBy(20.dp),
        contentPadding = PaddingValues(
            start = 12.dp,
            end = 12.dp,
            top = 15.dp,
            bottom = LocalBottomBarPadding.current
        )
    ) {
        items(items = recipes, key = { it.id }) {
            RecipeItem(it)
        }
    }
}

@Composable
fun ColumnScope.RecipeList(
    recipes: LazyPagingItems<RecipeEntity>,
    state: LazyListState = remember { LazyListState() },
) {
    LazyColumn(
        state = state,
        modifier = Modifier
            .fillMaxWidth()
            .weight(1f),
        verticalArrangement = Arrangement.spacedBy(20.dp),
        contentPadding = PaddingValues(
            start = 12.dp,
            end = 12.dp,
            top = 15.dp,
            bottom = LocalBottomBarPadding.current
        )
    ) {
        items(
            count = recipes.itemCount,
            key = { index -> recipes[index]?.id ?: index }
        ) { index ->
            val recipe = recipes[index]
            if (recipe != null) {
                RecipeItem(recipe)
            }
        }

        when {
            recipes.loadState.refresh is LoadState.Loading && recipes.itemCount == 0 -> items(6) {
                RecipeShimmer()
            }

            recipes.loadState.refresh is LoadState.Error && recipes.itemCount == 0 -> item {
                RecipeListError {
                    recipes.retry()
                }
            }

            recipes.loadState.append is LoadState.Loading -> item {
                RecipeListLoader()
            }

            recipes.loadState.append is LoadState.Error -> item {
                RecipeListError {
                    recipes.retry()
                }
            }
            recipes.itemCount == 0 -> item {
                RecipeListEmpty()
            }
        }
    }
}

@Composable
private fun RecipeListEmpty() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "Список пуст",
            color = Colors.BLUE_TEXT,
            style = Typography.H2
        )
    }
}

@Composable
private fun RecipeListError(
    modifier: Modifier = Modifier,
    onClickReload: () -> Unit
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp),
        contentAlignment = Alignment.Center
    ) {
        AppButton(
            modifier = Modifier.wrapContentSize(),
            text = "Загрузить снова",
        ) {
            onClickReload()
        }
    }
}

@Composable
private fun RecipeListLoader(
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
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
    AppAsyncImage(
        url = url,
        modifier = Modifier
            .size(100.dp)
            .clip(RoundedCornerShape(24.dp))
            .background(Colors.FORM),
        contentScale = ContentScale.Crop,
    )
}

