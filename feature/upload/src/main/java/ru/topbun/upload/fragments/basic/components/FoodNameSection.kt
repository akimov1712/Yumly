package ru.topbun.upload.fragments.basic.components

import android.R.attr.text
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ru.topbun.core.ui.components.AppOutlinedTextField
import ru.topbun.core.ui.components.AppTextField
import ru.topbun.upload.components.SectionWrapper

@Composable
fun FoodNameSection() = SectionWrapper("Название рецепта"){
    AppOutlinedTextField(
        modifier = Modifier.fillMaxWidth().defaultMinSize(minHeight = 56.dp),
        text = "",
        placeholder = "Введите название рецепта"
    ) { }
}