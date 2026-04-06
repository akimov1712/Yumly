package ru.topbun.home_filter.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.topbun.core.ui.R
import ru.topbun.core.ui.components.Height
import ru.topbun.core.ui.theme.Colors
import ru.topbun.core.ui.theme.Typography
import ru.topbun.core.ui.utils.formatRecipeDifficulty
import ru.topbun.core.ui.utils.rippleClickable
import ru.topbun.domain.entity.recipe.RecipeDifficulty
import ru.topbun.domain.entity.recipe.RecipeDifficulty.Easy
import ru.topbun.domain.entity.recipe.RecipeDifficulty.Hard
import ru.topbun.domain.entity.recipe.RecipeDifficulty.Normal
import ru.topbun.home_filter.HomeFilterIntent

@Composable
internal fun DifficultySection(
    difficultyList: List<RecipeDifficulty>,
    selectedDifficultyIndex: Int?,
    sendIntent: (HomeFilterIntent) -> Unit
) = Column {
    Title()
    Height(16.dp)
    DifficultyList(
        difficultyList = difficultyList,
        selectedDifficultyIndex = selectedDifficultyIndex,
        sendIntent = sendIntent
    )
}

@Composable
private fun DifficultyList(
    difficultyList: List<RecipeDifficulty>,
    selectedDifficultyIndex: Int?,
    sendIntent: (HomeFilterIntent) -> Unit
) {

    Row(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 24.dp),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        difficultyList.forEachIndexed { index, difficulty ->
            DifficultyItem(
                difficulty = difficulty,
                selected = selectedDifficultyIndex == index
            ) { sendIntent(HomeFilterIntent.ChangeSelectDifficultyIndex(index)) }
        }
    }
}

@Composable
private fun RowScope.DifficultyItem(
    difficulty: RecipeDifficulty,
    selected: Boolean,
    onClick: () -> Unit
) {
    val color = when(difficulty) {
        Easy -> Colors.GREEN
        Normal -> Colors.ORANGE
        Hard -> Colors.RED
    }

    Box(
        modifier = Modifier.weight(1f)
    ){
        if (selected){
            Box(
                modifier = Modifier.align(Alignment.TopEnd)
                    .padding(10.dp)
                    .size(16.dp)
                    .clip(CircleShape)
                    .background(color)
                    .padding(4.dp)
            ){
                Icon(
                    modifier = Modifier.fillMaxSize(),
                    painter = painterResource(R.drawable.ic_checkmark),
                    contentDescription = null,
                    tint = Colors.WHITE
                )
            }
        }
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp))
                .background(color.copy(0.05f))
                .rippleClickable(color, onClick)
                .border(
                    width = 2.dp,
                    color = color.copy(if (selected) 1f else 0.3f),
                    shape = RoundedCornerShape(16.dp),
                )
                .padding(top = 12.dp, bottom = 14.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(color.copy(0.2f))
                    .padding(10.dp),
                contentAlignment = Alignment.Center
            ){
                Icon(
                    modifier = Modifier.fillMaxSize(),
                    painter = painterResource(
                        when(difficulty){
                            Easy -> R.drawable.ic_difficulty_easy
                            Normal -> R.drawable.ic_difficulty_normal
                            Hard -> R.drawable.ic_difficulty_hard
                        }
                    ),
                    contentDescription = difficulty.name,
                    tint = color
                )
            }
            Height(4.dp)
            Text(
                modifier = Modifier.padding(horizontal = 5.dp),
                text = formatRecipeDifficulty(difficulty),
                color = color,
                style = Typography.H3.copy(fontSize = 14.sp)
            )
        }
    }
}

@Composable
private fun Title() {
    Text(
        text = "Сложность приготовления",
        modifier = Modifier.padding(horizontal = 24.dp),
        color = Colors.MAIN_TEXT,
        style = Typography.H2
    )
}

