package ru.topbun.upload.basic

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import ru.topbun.upload.UploadState
import ru.topbun.upload.components.DescriptionSection
import ru.topbun.upload.components.DurationSection
import ru.topbun.upload.components.FoodNameSection
import ru.topbun.upload.components.NutrientsSection
import ru.topbun.upload.components.PreviewPicker

@Composable
internal fun BasicScreen() = Column(
    verticalArrangement = Arrangement.spacedBy(20.dp)
){
    PreviewPicker()
    FoodNameSection()
    DescriptionSection()
    DurationSection()
    NutrientsSection(UploadState.NutrientsEnum.createNutrientMap())
}
