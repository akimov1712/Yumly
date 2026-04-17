package ru.topbun.upload.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ru.topbun.core.ui.components.DifficultyItem
import ru.topbun.domain.entity.recipe.RecipeDifficulty

@Composable
internal fun DifficultySection(
    difficultyList: List<RecipeDifficulty>,
    selectedDifficultyIndex: Int?,
    changeDifficultyIndex: (Int) -> Unit
) = SectionWrapper(
    title = "Сложность приготовления"
) {
    DifficultyList(
        difficultyList = difficultyList,
        selectedDifficultyIndex = selectedDifficultyIndex,
        changeDifficultyIndex = changeDifficultyIndex
    )
}

@Composable
private fun DifficultyList(
    difficultyList: List<RecipeDifficulty>,
    selectedDifficultyIndex: Int?,
    changeDifficultyIndex: (Int) -> Unit
) {

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        difficultyList.forEachIndexed { index, difficulty ->
            DifficultyItem(
                difficulty = difficulty,
                selected = selectedDifficultyIndex == index
            ) { changeDifficultyIndex(index) }
        }
    }
}