package ru.topbun.bmi

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import org.koin.compose.viewmodel.koinViewModel
import ru.topbun.bmi.components.AgeSection
import ru.topbun.bmi.components.BmiHeader
import ru.topbun.bmi.components.BmiResultCard
import ru.topbun.bmi.components.CalorieSection
import ru.topbun.bmi.components.GenderSelector
import ru.topbun.bmi.components.HealthyRangeCard
import ru.topbun.bmi.components.HeightSection
import ru.topbun.bmi.components.WeightSection
import ru.topbun.core.ui.theme.Colors

object BmiScreen : Screen {

    @Composable
    override fun Content() {
        val viewModel: BmiViewModel = koinViewModel()
        val state by viewModel.state.collectAsState()
        val navigator = LocalNavigator.currentOrThrow

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Colors.BACKGROUND)
                .systemBarsPadding()
        ) {
            BmiHeader(onClickBack = { navigator.pop() })

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 12.dp)
                    .padding(bottom = 24.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                BmiResultCard(state = state)
                GenderSelector(
                    selected = state.gender,
                    onSelect = { viewModel.sendIntent(BmiIntent.ChangeGender(it)) }
                )
                HeightSection(
                    height = state.heightCm,
                    onChange = { viewModel.sendIntent(BmiIntent.ChangeHeight(it)) }
                )
                WeightSection(
                    weight = state.weightKg,
                    onChange = { viewModel.sendIntent(BmiIntent.ChangeWeight(it)) }
                )
                AgeSection(
                    age = state.ageYears,
                    onChange = { viewModel.sendIntent(BmiIntent.ChangeAge(it)) }
                )
                HealthyRangeCard(
                    minKg = state.healthyMinKg,
                    maxKg = state.healthyMaxKg,
                    currentKg = state.weightKg
                )
                CalorieSection(
                    bmr = state.basalMetabolicRate,
                    daily = state.recommendedCalories
                )
            }
        }
    }
}
