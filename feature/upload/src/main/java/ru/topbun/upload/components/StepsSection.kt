package ru.topbun.upload.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.dropShadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.shadow.Shadow
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.rememberNestedScrollInteropConnection
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import coil3.compose.rememberAsyncImagePainter
import ru.topbun.core.ui.R
import ru.topbun.core.ui.components.AppOutlinedButton
import ru.topbun.core.ui.components.Height
import ru.topbun.core.ui.theme.Colors
import ru.topbun.core.ui.theme.Fonts
import ru.topbun.core.ui.utils.rippleClickable
import ru.topbun.domain.entity.recipe.StepEntity
import sh.calvin.reorderable.ReorderableColumn
import sh.calvin.reorderable.ReorderableListItemScope

@Composable
internal fun StepsSection(
    steps: List<StepEntity>,
    onClickAddStep: () -> Unit,
    onClickRemoveStep: (Int) -> Unit,
    onReorderSteps: (fromIndex: Int, toIndex: Int) -> Unit
) = SectionWrapper(
    title = buildAnnotatedString {
        append("Шаги приготовления")
        append(" ")
        withStyle(SpanStyle(color = Colors.SECONDARY_TEXT)) {
            append("(${steps.size})")
        }
    },
    padding = PaddingValues(horizontal = 12.dp)
) {
    ReorderableColumn(
        modifier = Modifier.zIndex(100f),
        list = steps,
        onSettle = { fromIndex, toIndex ->
            onReorderSteps(fromIndex, toIndex)
        },
    ) { index, step, isDragging ->
        key(step) {
            ReorderableItem{
                StepItem(
                    step = step,
                    position = index + 1,
                    isDragging = isDragging,
                    onClickRemove = { onClickRemoveStep(index) },
                )
            }
        }
    }
    if (steps.isNotEmpty()){
        Height(24.dp)
    }
    AppOutlinedButton(
        text = "Добавить шаг",
        modifier = Modifier
            .padding(horizontal = 12.dp)
            .fillMaxWidth()
            .defaultMinSize(minHeight = 56.dp),
        onClick = onClickAddStep,
        borderColor = Colors.OUTLINE,
        contentColor = Colors.BLUE_TEXT,
        startIcon = {
            Icon(
                modifier = Modifier.size(16.dp),
                painter = painterResource(R.drawable.ic_plus),
                contentDescription = null,
                tint = Colors.BLUE_TEXT
            )
        }
    )
}

@Composable
private fun ReorderableListItemScope.StepItem(
    step: StepEntity,
    position: Int,
    isDragging: Boolean,
    onClickRemove: () -> Unit,
) {
    Row(
        modifier = Modifier
            .dropShadow(
                shape = RoundedCornerShape(16.dp),
                shadow = Shadow(
                    radius = 8.dp,
                    color = Colors.BLACK.copy(0.2f),
                    alpha = if (isDragging) 1f else 0f
                )
            )
            .fillMaxWidth()
            .background(Colors.WHITE, RoundedCornerShape(16.dp))
            .padding(12.dp),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        PositionStep(position)
        StepValue(step)
        RoundActionButton(
            iconRes = R.drawable.ic_minus,
            containerColor = Colors.PRIMARY,
            onClick = onClickRemove
        )
    }
}

@Composable
private fun ReorderableListItemScope.PositionStep(position: Int) {
    Column(
        modifier = Modifier.draggableHandle(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Box(
            modifier = Modifier.size(24.dp)
                .clip(CircleShape)
                .background(Colors.BLUE_TEXT),
            contentAlignment = Alignment.Center
        ){
            Text(
                text = position.toString(),
                fontFamily = Fonts.INTER,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = Colors.WHITE
            )
        }
        ReorderHandle()
    }
}

@Composable
private fun RowScope.StepValue(step: StepEntity) {
    Column(
        modifier = Modifier.weight(1f),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ){
        SelectionContainer{
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = step.description,
                fontFamily = Fonts.INTER,
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                color = Colors.MAIN_TEXT,
                lineHeight = 15.sp,
                letterSpacing = 0.5.sp
            )
        }
        step.previewUrl?.let {
            Image(
                painter = rememberAsyncImagePainter(it),
                modifier = Modifier.fillMaxSize()
                    .clip(RoundedCornerShape(16.dp))
                    .heightIn(max = 400.dp),
                contentDescription = null,
                contentScale = ContentScale.FillWidth
            )
        }
    }
}

