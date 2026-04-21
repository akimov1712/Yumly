package ru.topbun.assistant.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ru.topbun.domain.entity.gpt.GptMessageEntity

@Composable
internal fun MessageList(
    messages: List<GptMessageEntity>,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 16.dp)
    ) {
        if (messages.isEmpty()) {
            items(4) {
                MessageShimmer()
            }
        } else {
            items(
                items = messages,
                key = { it.id }
            ) { message ->
                MessageBubble(message)
            }
        }
    }
}
