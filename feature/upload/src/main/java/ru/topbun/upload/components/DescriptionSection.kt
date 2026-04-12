package ru.topbun.upload.components

import android.R.attr.name
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.Job
import ru.topbun.core.ui.components.AppOutlinedTextField

@Composable
internal fun DescriptionSection(description: String, onChangeDescription: (String) -> Unit) = SectionWrapper("Описание"){
    AppOutlinedTextField(
        modifier = Modifier.fillMaxWidth().heightIn(130.dp, 160.dp),
        text = description,
        singleLine = false,
        shape = RoundedCornerShape(24.dp),
        placeholder = "Кратко опишите ваше блюдо",
        onValueChange = onChangeDescription,
        supportText = "${description.length}/500"
    )
}