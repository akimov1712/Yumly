package ru.topbun.core.ui.utils

import ru.topbun.domain.entity.recipe.RecipeDifficulty

private const val MINUTES_IN_HOUR = 60
private const val MINUTES_IN_DAY = 24 * MINUTES_IN_HOUR

fun formatCookingTime(totalMinutes: Int): String {
    if (totalMinutes <= 0) {
        return formatCountWithWord(0, "минута", "минуты", "минут")
    }

    val days = totalMinutes / MINUTES_IN_DAY
    val hours = (totalMinutes % MINUTES_IN_DAY) / MINUTES_IN_HOUR
    val minutes = totalMinutes % MINUTES_IN_HOUR

    return buildList {
        if (days > 0) add(formatCountWithWord(days, "день", "дня", "дней"))
        if (hours > 0) add(formatCountWithWord(hours, "час", "часа", "часов"))
        if (minutes > 0) add(formatCountWithWord(minutes, "минута", "минуты", "минут"))
    }.joinToString(separator = " ")
}

fun formatIngredientCount(count: Int): String =
    formatCountWithWord(count, "ингредиент", "ингредиента", "ингредиентов")

fun formatStepCount(count: Int): String =
    formatCountWithWord(count, "шаг", "шага", "шагов")

fun formatRecipeDifficulty(difficulty: RecipeDifficulty): String =
    when (difficulty) {
        RecipeDifficulty.Easy -> "Легко"
        RecipeDifficulty.Normal -> "Средне"
        RecipeDifficulty.Hard -> "Сложно"
    }

private fun formatCountWithWord(
    count: Int,
    singular: String,
    paucal: String,
    plural: String
): String = "$count ${selectPluralForm(count, singular, paucal, plural)}"

private fun selectPluralForm(
    count: Int,
    singular: String,
    paucal: String,
    plural: String
): String {
    val normalized = count % 100
    val lastDigit = normalized % 10

    return when {
        normalized in 11..14 -> plural
        lastDigit == 1 -> singular
        lastDigit in 2..4 -> paucal
        else -> plural
    }
}
