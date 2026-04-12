package ru.topbun.upload.basic

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.unit.dp
import org.koin.compose.viewmodel.koinViewModel
import ru.topbun.upload.UploadIntent
import ru.topbun.upload.UploadViewModel
import ru.topbun.upload.components.DescriptionSection
import ru.topbun.upload.components.DurationSection
import ru.topbun.upload.components.FoodNameSection
import ru.topbun.upload.components.NutrientsSection
import ru.topbun.upload.components.PreviewPicker

@Composable
internal fun BasicScreen(
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
}
