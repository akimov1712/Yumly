package ru.topbun.upload.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.StringAnnotation
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.unit.dp
import ru.topbun.core.ui.components.Height
import ru.topbun.core.ui.theme.Colors
import ru.topbun.core.ui.theme.Typography

@Composable
internal fun SectionWrapper(
    title: String,
    content: @Composable () -> Unit
) {
    SectionWrapper(title = buildAnnotatedString { append(title) }, content = content)
}

@Composable
internal fun SectionWrapper(
    title: AnnotatedString,
    padding: PaddingValues = PaddingValues(horizontal = 24.dp),
    content: @Composable () -> Unit
) {
    Column(
       modifier = Modifier.fillMaxWidth()
           .clip(RoundedCornerShape(28.dp))
           .background(Colors.WHITE)
           .padding(top = 20.dp, bottom = 24.dp)
    ) {
        Text(
            modifier = Modifier.padding(horizontal = 24.dp),
            text = title,
            color = Colors.MAIN_TEXT,
            style = Typography.H2
        )
        Height(20.dp)
        Column(Modifier.padding(padding)){
            content()
        }
    }
}