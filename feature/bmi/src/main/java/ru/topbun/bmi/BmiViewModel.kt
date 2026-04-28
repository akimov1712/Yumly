package ru.topbun.bmi

import kotlinx.coroutines.flow.update
import ru.topbun.core.android.MVI

internal class BmiViewModel : MVI<BmiIntent, BmiState, BmiEvent>(BmiState()) {

    private fun changeGender(value: BmiState.Gender) =
        _state.update { it.copy(gender = value) }

    private fun changeHeight(value: Int) =
        _state.update { it.copy(heightCm = value.coerceIn(MIN_HEIGHT, MAX_HEIGHT)) }

    private fun changeWeight(value: Float) =
        _state.update { it.copy(weightKg = value.coerceIn(MIN_WEIGHT, MAX_WEIGHT)) }

    private fun changeAge(value: Int) =
        _state.update { it.copy(ageYears = value.coerceIn(MIN_AGE, MAX_AGE)) }

    override suspend fun handleIntent(intent: BmiIntent) {
        when (intent) {
            is BmiIntent.ChangeGender -> changeGender(intent.value)
            is BmiIntent.ChangeHeight -> changeHeight(intent.value)
            is BmiIntent.ChangeWeight -> changeWeight(intent.value)
            is BmiIntent.ChangeAge -> changeAge(intent.value)
        }
    }

    companion object {
        const val MIN_HEIGHT = 100
        const val MAX_HEIGHT = 230
        const val MIN_WEIGHT = 30f
        const val MAX_WEIGHT = 200f
        const val MIN_AGE = 10
        const val MAX_AGE = 100
    }
}
