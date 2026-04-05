package ru.topbun.home_filter

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import ru.topbun.core.ui.components.BottomDialogWrapper
import ru.topbun.core.ui.components.Height
import ru.topbun.core.ui.theme.Colors
import ru.topbun.core.ui.theme.Typography
import ru.topbun.home_filter.components.CaloriesSection
import ru.topbun.home_filter.components.CategorySection
import ru.topbun.home_filter.components.DurationSection

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
        Height(20.dp)
        DurationSection()
        Height(20.dp)
        CaloriesSection()
    }
}


@Composable
private fun Title() {
    Text(
        modifier = Modifier.fillMaxWidth(),
        text = "Добавить фильтр",
        color = Colors.MAIN_TEXT,
        style = Typography.H2,
        textAlign = TextAlign.Center
    )
}


