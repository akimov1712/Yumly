package ru.topbun.home_filter.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import ru.topbun.core.ui.components.AppRangeSlider
import ru.topbun.core.ui.components.Height
import ru.topbun.core.ui.theme.Colors
import ru.topbun.core.ui.theme.Typography
import ru.topbun.home_filter.HomeFilterIntent

@Composable
internal fun CaloriesSection(
    minCaloriesFromProgress: Int,
    maxCaloriesFromProgress: Int,
    minCaloriesProgress: Float,
    maxCaloriesProgress: Float,
    sendIntent: (HomeFilterIntent) -> Unit
) = Column {
    Title()
    Height(16.dp)
    RowValues(minCaloriesFromProgress, maxCaloriesFromProgress)
    AppRangeSlider(
        value = minCaloriesProgress..maxCaloriesProgress
    ){
        sendIntent(HomeFilterIntent.ChangeLimitCalories(it.start, it.endInclusive))
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