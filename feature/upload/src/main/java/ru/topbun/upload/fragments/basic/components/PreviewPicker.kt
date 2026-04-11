package ru.topbun.upload.fragments.basic.components

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import ru.topbun.core.ui.components.Height
import ru.topbun.core.ui.theme.Colors
import ru.topbun.core.ui.utils.rippleClickable

@Composable
internal fun PreviewPicker() = Box(
    modifier = Modifier.fillMaxWidth()
        .aspectRatio(1.6f)
        .clip(RoundedCornerShape(28.dp))
        .border(1.dp, Colors.SECONDARY_TEXT, RoundedCornerShape(28.dp))
        .rippleClickable(Colors.BLACK){  },
    contentAlignment = Alignment.Center
){
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            modifier = Modifier.size(60.dp),
            painter = painterResource(ru.topbun.core.ui.R.drawable.ic_image_picker),
            contentDescription = "image_picker",
            tint = Colors.SECONDARY_TEXT
        )
        Height(16.dp)
        Text(
            text = "Добавить обложку",
            color = Colors.MAIN_TEXT,
            style = ru.topbun.core.ui.theme.Typography.H3
        )
        Height(10.dp)
        Text(
            text = "(до 12 Mb)",
            color = Colors.SECONDARY_TEXT,
            style = ru.topbun.core.ui.theme.Typography.S
        )
    }
}