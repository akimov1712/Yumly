package ru.topbun.upload.fragments

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.koin.compose.viewmodel.koinViewModel
import ru.topbun.core.ui.components.AppButton
import ru.topbun.upload.UploadIntent
import ru.topbun.upload.UploadViewModel
import ru.topbun.upload.components.DescriptionSection
import ru.topbun.upload.components.DifficultySection
import ru.topbun.upload.components.DurationSection
import ru.topbun.upload.components.FoodNameSection
import ru.topbun.upload.components.NutrientsSection
import ru.topbun.upload.components.PreviewPicker

@Composable
internal fun BasicFragment(
    viewModel: UploadViewModel = koinViewModel()
) = Column(
    verticalArrangement = Arrangement.spacedBy(20.dp)
){
    val state by viewModel.state.collectAsState()

    PreviewPicker(previewUri = state.preview){
        viewModel.sendIntent(UploadIntent.ChangePreview(it))
    }
    FoodNameSection(name = state.name){
        viewModel.sendIntent(UploadIntent.ChangeName(it))
    }
    DescriptionSection(description = state.description){
        viewModel.sendIntent(UploadIntent.ChangeDescription(it))
    }
    DurationSection(cookingTime = state.cookingTime){
        viewModel.sendIntent(UploadIntent.ChangeCookingTime(it))
    }
    NutrientsSection(
        nutrients = state.nutrients,
        sumLimitExceeded = state.sumLimitExceeded,
        totalCalories = state.totalCalories
    ){ value, nutrient ->
        viewModel.sendIntent(UploadIntent.ChangeNutrientValue(nutrient, value))
    }
    DifficultySection(
        difficultyList = state.difficultyList,
        selectedDifficultyIndex = state.selectedDifficultyIndex,
    ){
        viewModel.sendIntent(UploadIntent.ChangeSelectDifficultyIndex(it))
    }
    AppButton(
        modifier = Modifier
            .fillMaxWidth()
            .defaultMinSize(minHeight = 56.dp)
            .padding(horizontal = 12.dp),
        enabled = state.nextButtonEnabled,
        text = "Далее",
    ) {
        viewModel.sendIntent(UploadIntent.ChangeFragment(UploadFragments.Content))
    }
}
