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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.compose.ui.window.SecureFlagPolicy
import ru.topbun.core.ui.theme.Colors

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DialogWrapper(
    onDismissRequest: () -> Unit,
    containerColor: Color = Colors.WHITE,
    content: @Composable () -> Unit
) {

    Dialog(
        onDismissRequest = onDismissRequest,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ){
        Box(
            modifier = Modifier.fillMaxWidth()
                .padding(32.dp)
                .clip(RoundedCornerShape(32.dp))
                .background(containerColor),
            contentAlignment = Alignment.Center
        ){
            content()
        }
    }
}
