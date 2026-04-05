package ru.topbun.home_filter.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
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


@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun DurationSection() = Column {
    val viewModel: HomeFilterViewModel = koinViewModel()
    val state by viewModel.state.collectAsState()

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
            ){
                append("(в минутах)")
            }
        },
        modifier = Modifier.padding(horizontal = 24.dp),
        color = Colors.MAIN_TEXT,
        style = Typography.H2
    )
    Height(16.dp)
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp),
    ){
        Text(
            modifier = Modifier.fillMaxWidth(),
            text = when{
                state.progressToDuration <= 10 -> "<10"
                state.progressToDuration >= 60 -> ">60"
                else -> state.progressToDuration.toString()
            },
            color = Colors.PRIMARY,
            style = Typography.H3,
            textAlign = TextAlign.Center
        )

        Text(
            modifier = Modifier.align(Alignment.CenterStart),
            text = "<10",
            color = if (state.progressToDuration > 10) Colors.PRIMARY else Colors.SECONDARY_TEXT,
            style = Typography.H3
        )
        Text(
            modifier = Modifier.align(Alignment.CenterEnd),
            text = ">60",
            color = if (state.progressToDuration < 60) Colors.SECONDARY_TEXT else Colors.PRIMARY,
            style = Typography.H3
        )
    }
    AppSlider(state.durationProgress){
        viewModel.sendIntent(HomeFilterIntent.ChangeDuration(it))
    }
}

