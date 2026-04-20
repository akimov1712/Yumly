package ru.topbun.upload.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import ru.topbun.core.ui.theme.Colors
import sh.calvin.reorderable.ReorderableListItemScope

@Composable
internal fun ReorderHandle() {
    Column(
        modifier = Modifier,
        verticalArrangement = Arrangement.spacedBy(3.dp)
    ) {
        repeat(3) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(3.dp)
            ) {
                repeat(2) {
                    Box(
                        modifier = Modifier
                            .size(4.dp)
                            .clip(CircleShape)
                            .background(Colors.SECONDARY_TEXT)
                    )
                }
            }
        }
    }
}
