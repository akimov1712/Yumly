package ru.topbun.upload.fragments.basic.components

import android.R.attr.text
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ru.topbun.core.ui.components.AppOutlinedTextField
import ru.topbun.core.ui.components.AppTextField
import ru.topbun.upload.components.SectionWrapper

@Composable
fun DescriptionSection() = SectionWrapper("Описание"){
    AppOutlinedTextField(
        modifier = Modifier.fillMaxWidth().heightIn(100.dp, 120.dp),
        text = "",
        singleLine = false,
        shape = RoundedCornerShape(24.dp),
        placeholder = "Кратко опишите ваше блюдо"
    ) { }
}