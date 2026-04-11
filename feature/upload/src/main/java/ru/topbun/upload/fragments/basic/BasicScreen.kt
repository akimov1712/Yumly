package ru.topbun.upload.fragments.basic

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import ru.topbun.core.ui.components.Height
import ru.topbun.upload.fragments.basic.components.DescriptionSection
import ru.topbun.upload.fragments.basic.components.DurationSection
import ru.topbun.upload.fragments.basic.components.FoodNameSection
import ru.topbun.upload.fragments.basic.components.PreviewPicker

@Composable
internal fun BasicScreen() = Column{
    PreviewPicker()
    Height(32.dp)
    FoodNameSection()
    Height(20.dp)
    DescriptionSection()
    Height(20.dp)
    DurationSection()
}
