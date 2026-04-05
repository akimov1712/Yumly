package ru.topbun.home_filter.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.koin.compose.viewmodel.koinViewModel
import ru.topbun.core.ui.components.AppSlider
import ru.topbun.core.ui.components.Height
import ru.topbun.core.ui.theme.Colors
import ru.topbun.core.ui.theme.Fonts
import ru.topbun.core.ui.theme.Typography
import ru.topbun.home_filter.HomeFilterIntent
import ru.topbun.home_filter.HomeFilterViewModel

@Composable
internal fun DurationSection() = Column {
    val viewModel: HomeFilterViewModel = koinViewModel()
    val state by viewModel.state.collectAsState()

    Title()
    Height(16.dp)
    RowValues(state.durationFromProgress)
    AppSlider(state.durationProgress){
        viewModel.sendIntent(HomeFilterIntent.ChangeDuration(it))
    }
}

@Composable
private fun RowValues(duration: Int) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp),
    ) {
        Text(
            modifier = Modifier.fillMaxWidth(),
            text = when {
                duration <= 10 -> "<10"
                duration >= 60 -> ">60"
                else -> duration.toString()
            },
            color = Colors.PRIMARY,
            style = Typography.H3,
            textAlign = TextAlign.Center
        )

        Text(
            modifier = Modifier.align(Alignment.CenterStart),
            text = "<10",
            color = if (duration > 10) Colors.PRIMARY else Colors.SECONDARY_TEXT,
            style = Typography.H3
        )
        Text(
            modifier = Modifier.align(Alignment.CenterEnd),
            text = ">60",
            color = if (duration < 60) Colors.SECONDARY_TEXT else Colors.PRIMARY,
            style = Typography.H3
        )
    }
}

@Composable
private fun Title() {
    Text(
        text = buildAnnotatedString {
            append("Макс. время приготовления ")
            withStyle(
                SpanStyle(
                    color = Colors.SECONDARY_TEXT,
                    fontFamily = Fonts.INTER,
                    fontWeight = FontWeight.Medium,
                    fontSize = 17.sp,
                )
            ) {
                append("(в минутах)")
            }
        },
        modifier = Modifier.padding(horizontal = 24.dp),
        color = Colors.MAIN_TEXT,
        style = Typography.H2
    )
}

