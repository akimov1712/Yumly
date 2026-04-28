package ru.topbun.bmi

import kotlin.math.pow
import kotlin.math.roundToInt

internal data class BmiState(
    val gender: Gender = Gender.Female,
    val heightCm: Int = 170,
    val weightKg: Float = 65f,
    val ageYears: Int = 25,
) {

    val bmi: Float
        get() {
            val meters = heightCm / 100f
            if (meters <= 0f) return 0f
            return (weightKg / meters.pow(2)).coerceAtLeast(0f)
        }

    val bmiRounded: Float
        get() = (bmi * 10f).roundToInt() / 10f

    val category: BmiCategory
        get() = BmiCategory.fromBmi(bmi)

    val categoryProgress: Float
        get() {
            val min = 14f
            val max = 40f
            return ((bmi - min) / (max - min)).coerceIn(0f, 1f)
        }

    val healthyMinKg: Float
        get() {
            val meters = heightCm / 100f
            return 18.5f * meters.pow(2)
        }

    val healthyMaxKg: Float
        get() {
            val meters = heightCm / 100f
            return 24.9f * meters.pow(2)
        }

    val basalMetabolicRate: Int
        get() {
            val genderConst = if (gender == Gender.Male) 5 else -161
            val rate = 10 * weightKg + 6.25f * heightCm - 5f * ageYears + genderConst
            return rate.roundToInt().coerceAtLeast(0)
        }

    val recommendedCalories: Int
        get() = (basalMetabolicRate * 1.375f).roundToInt()

    enum class Gender(val title: String) {
        Female("Женщина"), Male("Мужчина");
    }
}

internal enum class BmiCategory(
    val title: String,
    val description: String,
    val rangeStart: Float,
    val rangeEnd: Float,
) {
    Underweight(
        title = "Недостаток веса",
        description = "Старайтесь питаться чаще и сбалансировано",
        rangeStart = 0f,
        rangeEnd = 18.5f,
    ),
    Normal(
        title = "Нормальный вес",
        description = "Отлично! Поддерживайте текущий ритм",
        rangeStart = 18.5f,
        rangeEnd = 25f,
    ),
    Overweight(
        title = "Избыточный вес",
        description = "Подумайте о небольших изменениях в рационе",
        rangeStart = 25f,
        rangeEnd = 30f,
    ),
    Obese(
        title = "Ожирение",
        description = "Рекомендуем проконсультироваться со специалистом",
        rangeStart = 30f,
        rangeEnd = Float.MAX_VALUE,
    );

    companion object {
        fun fromBmi(value: Float): BmiCategory = entries.first { value < it.rangeEnd }
    }
}

internal sealed interface BmiIntent {

    data class ChangeGender(val value: BmiState.Gender) : BmiIntent
    data class ChangeHeight(val value: Int) : BmiIntent
    data class ChangeWeight(val value: Float) : BmiIntent
    data class ChangeAge(val value: Int) : BmiIntent
}

internal sealed interface BmiEvent
