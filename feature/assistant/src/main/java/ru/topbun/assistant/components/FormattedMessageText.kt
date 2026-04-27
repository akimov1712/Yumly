package ru.topbun.assistant.components

import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.sp
import ru.topbun.core.ui.theme.Typography

@Composable
internal fun FormattedMessageText(
    text: String,
    color: Color
) {
    SelectionContainer{
        Text(
            text = text.toFormattedMessage(),
            color = color,
            style = Typography.P2
        )
    }
}

private fun String.toFormattedMessage(): AnnotatedString = buildAnnotatedString {
    val lines = trim().lines()
    lines.forEachIndexed { index, line ->
        appendLine(line)
        if (index != lines.lastIndex) {
            append('\n')
        }
    }
}

internal fun String.toPlainMessagePreview(): String =
    toFormattedMessage().text

private fun AnnotatedString.Builder.appendLine(line: String) {
    val trimmed = line.trimEnd()
    val content = trimmed.removeHeadingMarker()
    val isHeading = content != trimmed
    val isBullet = content.trimStart().startsWith("- ")

    val normalized = if (isBullet) {
        content.replaceFirst("-", "•")
    } else {
        content
    }

    val spanStyle = if (isHeading) {
        SpanStyle(
            fontWeight = FontWeight.Bold,
            fontSize = 17.sp
        )
    } else {
        SpanStyle()
    }

    withStyle(spanStyle) {
        appendInlineMarkdown(normalized)
    }
}

private fun String.removeHeadingMarker(): String {
    val trimmed = trimStart()
    val headingLevel = trimmed.takeWhile { it == '#' }.length
    return if (
        headingLevel in 1..6 &&
        trimmed.getOrNull(headingLevel) == ' '
    ) {
        trimmed.drop(headingLevel + 1)
    } else {
        this
    }
}

private fun AnnotatedString.Builder.appendInlineMarkdown(text: String) {
    var index = 0
    while (index < text.length) {
        val start = text.indexOf("**", startIndex = index)
        if (start == -1) {
            append(text.substring(index))
            return
        }

        append(text.substring(index, start))

        val end = text.indexOf("**", startIndex = start + 2)
        if (end == -1) {
            append(text.substring(start + 2))
            return
        }

        withStyle(SpanStyle(fontWeight = FontWeight.Bold)) {
            append(text.substring(start + 2, end))
        }
        index = end + 2
    }
}
