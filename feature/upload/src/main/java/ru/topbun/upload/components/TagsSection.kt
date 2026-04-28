package ru.topbun.upload.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import ru.topbun.core.ui.components.AppButton
import ru.topbun.core.ui.components.AppTextButton
import ru.topbun.core.ui.theme.Colors
import ru.topbun.core.ui.theme.Typography
import ru.topbun.core.ui.utils.rippleClickable
import ru.topbun.domain.ScreenUiState
import ru.topbun.domain.entity.recipe.tag.TagRecipeEntity
import ru.topbun.domain.entity.recipe.tag.TagType

@Composable
internal fun TagsSection(
    tags: List<TagRecipeEntity>,
    selectedIds: List<Int>,
    status: ScreenUiState,
    onToggle: (Int) -> Unit,
    onRetry: () -> Unit,
) {
    val title = buildAnnotatedString {
        append("Теги ")
        withStyle(SpanStyle(color = Colors.SECONDARY_TEXT)) {
            append("(${selectedIds.size})")
        }
    }

    SectionWrapper(title = title) {
        when (status) {
            ScreenUiState.Loading -> TagsLoading()
            ScreenUiState.Error -> TagsError(onRetry = onRetry)
            ScreenUiState.Success -> {
                if (tags.isEmpty()) {
                    Text(
                        text = "Тегов пока нет",
                        style = Typography.P2,
                        color = Colors.SECONDARY_TEXT
                    )
                } else {
                    TagsGrouped(
                        tags = tags,
                        selectedIds = selectedIds,
                        onToggle = onToggle,
                    )
                }
            }
            else -> Unit
        }
    }
}

@Composable
private fun TagsGrouped(
    tags: List<TagRecipeEntity>,
    selectedIds: List<Int>,
    onToggle: (Int) -> Unit,
) {
    val grouped = tags.groupBy { it.type }

    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        grouped.forEach { (type, groupTags) ->
            TagGroup(
                title = type.title,
                tags = groupTags,
                selectedIds = selectedIds,
                onToggle = onToggle
            )
        }
    }
}

@Composable
private fun TagGroup(
    title: String,
    tags: List<TagRecipeEntity>,
    selectedIds: List<Int>,
    onToggle: (Int) -> Unit,
) {
    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        Text(
            text = title,
            style = Typography.H3,
            color = Colors.SECONDARY_TEXT
        )
        FlowRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            tags.forEach { tag ->
                TagChip(
                    tag = tag,
                    isSelected = selectedIds.contains(tag.id),
                    onClick = { onToggle(tag.id) }
                )
            }
        }
    }
}

@Composable
private fun TagChip(
    tag: TagRecipeEntity,
    isSelected: Boolean,
    onClick: () -> Unit,
) {
    val bgColor = if (isSelected) Colors.PRIMARY else Color.Transparent
    val borderColor = if (isSelected) Colors.PRIMARY else Colors.OUTLINE
    val textColor = if (isSelected) Colors.WHITE else Colors.SECONDARY_TEXT
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(28.dp))
            .border(2.dp, borderColor, RoundedCornerShape(28.dp))
            .background(bgColor)
            .rippleClickable(color = Colors.WHITE, onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 10.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = tag.name,
            color = textColor,
            style = Typography.P2
        )
    }
}

@Composable
private fun TagsLoading() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp),
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
private fun TagsError(onRetry: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp),
        contentAlignment = Alignment.Center
    ) {
        AppButton(
            modifier = Modifier.wrapContentSize(),
            text = "Загрузить снова",
            onClick = onRetry
        )
    }
}

private val TagType.title: String
    get() = when (this) {
        TagType.Category -> "Категории"
        TagType.Diets -> "Диеты"
        TagType.Preparation -> "Способ приготовления"
    }
