package ru.topbun.core.ui.components

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.topbun.core.ui.theme.Colors
import ru.topbun.core.ui.theme.Typography

@Composable
fun AppSnackbarHost(
    snackbarHostState: SnackbarHostState,
    modifier: Modifier = Modifier
) {
    SnackbarHost(
        modifier = modifier,
        hostState = snackbarHostState
    ) {
        Snackbar(
            shape = RoundedCornerShape(12.dp),
            containerColor = Color(0xFF393939),
            contentColor = Colors.WHITE,
        ) {
            Text(
                text = it.visuals.message,
                color = Colors.WHITE,
                style = Typography.H3,
                fontSize = 14.sp,
            )
        }
    }
}