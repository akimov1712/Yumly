package ru.topbun.core.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.ModalBottomSheetProperties
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.SecureFlagPolicy
import ru.topbun.core.ui.theme.Colors

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottomDialogWrapper(
    onDismissRequest: () -> Unit,
    containerColor: Color = Colors.WHITE,
    content: @Composable ColumnScope.() -> Unit
) {

    ModalBottomSheet(
        onDismissRequest = onDismissRequest,
        shape = RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp),
        containerColor = containerColor,
        contentColor = Colors.PRIMARY,
        dragHandle = {
            Box(
                modifier = Modifier.fillMaxWidth().padding(20.dp),
                contentAlignment = Alignment.Center
            ){
                Spacer(Modifier.size(30.dp, 4.dp).background(Colors.PRIMARY, CircleShape))
            }
        },
        tonalElevation = 4.dp,
        content = content,
    )
}
