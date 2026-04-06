package ru.topbun.home_filter.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import ru.topbun.core.ui.components.AppButton
import ru.topbun.core.ui.components.AppTextButton
import ru.topbun.core.ui.components.Height
import ru.topbun.core.ui.theme.Colors
import ru.topbun.core.ui.theme.Typography
import ru.topbun.core.ui.utils.rippleClickable
import ru.topbun.domain.entity.recipe.tag.TagRecipeEntity
import ru.topbun.home_filter.HomeFilterIntent
import ru.topbun.home_filter.HomeFilterState
import ru.topbun.home_filter.HomeFilterState.CategoryUiState.*

@Composable
internal fun CategorySection(
    isExpanded: Boolean,
    categoryUiState: HomeFilterState.CategoryUiState,
    sortedCategories: List<TagRecipeEntity>,
    selectedCategoriesIds: List<Int>,
    sendIntent: (HomeFilterIntent) -> Unit,
) = Column {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = "Категории",
            color = Colors.MAIN_TEXT,
            style = Typography.H2
        )
        AppTextButton(
            text = if (isExpanded) "Скрыть" else "Показать все"
        ) { sendIntent(HomeFilterIntent.ChangeExpandedCategoryList) }
    }
    Height(16.dp)
    when(categoryUiState){
        Idle -> {}
        Error -> CategoryError{ sendIntent(HomeFilterIntent.LoadCategories) }
        Loading -> CategoryLoading()
        Success -> CategorySuccess(
            isExpanded = isExpanded,
            sortedCategories = sortedCategories,
            selectedCategoriesIds = selectedCategoriesIds,
            sendIntent = sendIntent
        )
    }

}

@Composable
private fun CategoryError(onClick: () -> Unit) {
    AppButton(
        modifier = Modifier.wrapContentSize(),
        text = "Загрузить снова",
    ) {
        onClick()
    }
}

@Composable
private fun CategoryLoading() {
    Box(Modifier.fillMaxWidth(), Alignment.Center){
        CircularProgressIndicator(
            modifier = Modifier.size(24.dp),
            strokeWidth = 2.5.dp,
            trackColor = Colors.PRIMARY
        )
    }
}

@Composable
private fun CategorySuccess(
    isExpanded: Boolean,
    sortedCategories: List<TagRecipeEntity>,
    selectedCategoriesIds: List<Int>,
    sendIntent: (HomeFilterIntent) -> Unit
) {
    if (isExpanded){
        CategoryFlowColumn(sortedCategories, selectedCategoriesIds){
            sendIntent(HomeFilterIntent.ChangeSelectedCategory(it))
        }
    } else {
        CategoryLazyRow(sortedCategories, selectedCategoriesIds){
            sendIntent(HomeFilterIntent.ChangeSelectedCategory(it))
        }
    }
}

@Composable
private fun CategoryLazyRow(
    categories: List<TagRecipeEntity>,
    categoriesSelected: List<Int>,
    onClick: (id: Int) -> Unit
) {
    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        contentPadding = PaddingValues(horizontal = 24.dp)
    ) {
        items(items = categories){
            CategoryItem(
                tag = it,
                isSelected = categoriesSelected.contains(it.id),
                onClick = onClick
            )
        }
    }
}

@Composable
private fun CategoryFlowColumn(
    categories: List<TagRecipeEntity>,
    categoriesSelected: List<Int>,
    onClick: (id: Int) -> Unit
) {
    FlowRow(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp),
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        categories.forEach {
            CategoryItem(
                tag = it,
                isSelected = categoriesSelected.contains(it.id),
                onClick = onClick
            )
        }
    }
}

@Composable
private fun CategoryItem(
    tag: TagRecipeEntity,
    isSelected: Boolean,
    onClick: (Int) -> Unit
) {
    val bgColor = if (isSelected) Colors.PRIMARY else Color.Transparent
    val borderColor = if (isSelected) Colors.PRIMARY else Colors.OUTLINE
    val textColor = if (isSelected) Colors.WHITE else Colors.SECONDARY_TEXT
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(32.dp))
            .border(2.dp, borderColor, RoundedCornerShape(32.dp))
            .background(bgColor)
            .rippleClickable { onClick(tag.id) }
            .padding(24.dp, 15.dp),
        contentAlignment = Alignment.Center
    ){
        Text(
            text = tag.name,
            color = textColor,
            style = Typography.H3
        )
    }
}