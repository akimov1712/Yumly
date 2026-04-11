package ru.topbun.upload.components

import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ru.topbun.core.ui.components.AppOutlinedTextField

@Composable
internal fun FoodNameSection() = SectionWrapper("Название рецепта"){
    AppOutlinedTextField(
        modifier = Modifier.fillMaxWidth().defaultMinSize(minHeight = 56.dp),
        text = "",
        placeholder = "Введите название рецепта"
    ) { }
}