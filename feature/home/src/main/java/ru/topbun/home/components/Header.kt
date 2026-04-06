package ru.topbun.home.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ru.topbun.core.ui.R
import ru.topbun.core.ui.components.AppOutlinedTextField
import ru.topbun.core.ui.components.AppTextField
import ru.topbun.core.ui.theme.Colors

@Composable
internal fun Header(
    text: String,
    isFilterChanged: Boolean,
    onClickFilter: () -> Unit,
    onValueChange: (String) -> Unit
) {
    Row(
        modifier = Modifier
            .padding(horizontal = 12.dp).padding(top = 20.dp)
            .fillMaxWidth()
            .clip(RoundedCornerShape(44.dp))
            .background(Colors.WHITE)
            .padding(10.dp),
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        AppTextField(
            text = text,
            onValueChange = onValueChange,
            placeholder = "Search",
            modifier = Modifier.weight(1f)
                .defaultMinSize(minHeight = 56.dp),
            startIcon = painterResource(R.drawable.ic_search),
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
        )
        Box {
            if (isFilterChanged){
                Box(
                    Modifier.align(Alignment.TopEnd)
                        .padding(4.dp)
                        .size(10.dp)
                        .clip(CircleShape)
                        .background(Colors.PRIMARY)
                )
            }
            IconButton(
                modifier = Modifier.size(56.dp),
                onClick = onClickFilter
            ) {
                Icon(
                    modifier = Modifier.fillMaxSize()
                    .padding(12.dp),
                    painter = painterResource(R.drawable.ic_filter),
                    contentDescription = null,
                    tint = Colors.MAIN_TEXT
                )
            }
        }
    }
}