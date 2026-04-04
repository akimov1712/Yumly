package ru.topbun.home_filter

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.rememberNestedScrollInteropConnection
import androidx.compose.ui.unit.dp
import ru.topbun.core.ui.components.BottomDialogWrapper
import ru.topbun.core.ui.components.Height
import ru.topbun.home_filter.components.CategorySection
import ru.topbun.home_filter.components.Title


@Composable
fun HomeFilterDialog(
    onDismissRequest: () -> Unit
) = BottomDialogWrapper(
    onDismissRequest = onDismissRequest
){
    Column(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Title()
        Height(20.dp)
        FilterContent()
    }
}

@Composable
internal fun FilterContent() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState())
            .padding(top = 20.dp, bottom = 32.dp)
    ) {
        CategorySection()
    }
}




