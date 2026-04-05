package ru.topbun.home_filter.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.RangeSlider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import org.koin.compose.viewmodel.koinViewModel
import ru.topbun.core.ui.components.AppRangeSlider
import ru.topbun.core.ui.components.Height
import ru.topbun.core.ui.theme.Colors
import ru.topbun.core.ui.theme.Typography
import ru.topbun.home_filter.HomeFilterIntent
import ru.topbun.home_filter.HomeFilterState
import ru.topbun.home_filter.HomeFilterViewModel
import ru.topbun.home_filter.components.RowValues

@Composable
internal fun CaloriesSection() = Column {
    val viewModel: HomeFilterViewModel = koinViewModel()
    val state by viewModel.state.collectAsState()
    Title()
    Height(16.dp)
    RowValues(state.minCaloriesFromProgress, state.maxCaloriesFromProgress)
    AppRangeSlider(
        value = state.minCaloriesProgress..state.maxCaloriesProgress
    ){
        viewModel.sendIntent(HomeFilterIntent.ChangeLimitCalories(it.start, it.endInclusive))
    }
}


@Composable
private fun RowValues(
    minCalories: Int,
    maxCalories: Int,
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp),
    ) {
        Text(
            modifier = Modifier.align(Alignment.CenterStart),
            text = minCalories.toString(),
            color = Colors.PRIMARY,
            style = Typography.H3
        )
        Text(
            modifier = Modifier.align(Alignment.CenterEnd),
            text = if (maxCalories >= 1000) "∞" else maxCalories.toString(),
            color = Colors.PRIMARY,
            style = Typography.H3
        )
    }
}


@Composable
private fun Title() {
    Text(
        text = "Граница калорий",
        modifier = Modifier.padding(horizontal = 24.dp),
        color = Colors.MAIN_TEXT,
        style = Typography.H2
    )
}