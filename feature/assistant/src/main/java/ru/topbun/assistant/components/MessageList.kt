package ru.topbun.assistant.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.times
import com.valentinilk.shimmer.ShimmerBounds
import com.valentinilk.shimmer.rememberShimmer
import com.valentinilk.shimmer.shimmer
import ru.topbun.core.ui.components.Height
import ru.topbun.core.ui.theme.Colors
import ru.topbun.core.ui.theme.Typography
import ru.topbun.domain.entity.gpt.GptMessageEntity
import ru.topbun.domain.entity.gpt.GptMessageRoleType

@Composable
internal fun MessageList(
    contentPadding: PaddingValues,
    messages: List<GptMessageEntity>,
    state: LazyListState,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        state = state,
        verticalArrangement = Arrangement.spacedBy(12.dp),
        contentPadding = contentPadding
    ) {
        items(
            items = messages,
            key = { it.id }
        ) { message ->
            MessageItem(message)
        }
    }
}


@Composable
private fun MessageItem(message: GptMessageEntity) {
    val isUser = message.role == GptMessageRoleType.USER
    val isAssistantLoading = message.role == GptMessageRoleType.ASSISTANT && message.text.isBlank()

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = if (isUser) Arrangement.End else Arrangement.Start
    ) {
        Column(
            modifier = Modifier
                .widthIn(max = 0.8f * LocalConfiguration.current.screenWidthDp.dp)
                .clip(
                    RoundedCornerShape(
                        topStart = 26.dp,
                        topEnd = 26.dp,
                        bottomStart = if (isUser) 26.dp else 10.dp,
                        bottomEnd = if (isUser) 10.dp else 26.dp
                    )
                )
                .background(if (isUser) Colors.PRIMARY else Colors.WHITE)
                .padding(horizontal = 18.dp, vertical = 14.dp)
        ) {
            if (isAssistantLoading) {
                AssistantMessageLoader()
            } else {
                FormattedMessageText(
                    text = message.text,
                    color = if (isUser) Colors.WHITE else Colors.MAIN_TEXT
                )
            }

            Height(8.dp)

            Text(
                modifier = Modifier.align(if (isUser) Alignment.End else Alignment.Start),
                text = message.createdAt.toMessageDate(),
                color = if (isUser) Colors.WHITE.copy(alpha = 0.72f) else Colors.SECONDARY_TEXT,
                style = Typography.S
            )
        }
    }
}

@Composable
private fun AssistantMessageLoader() {
    val shimmer = rememberShimmer(shimmerBounds = ShimmerBounds.Window)
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Box(
            Modifier
                .fillMaxWidth(0.9f)
                .clip(RoundedCornerShape(8.dp))
                .shimmer(shimmer)
                .background(Colors.SHIMMER)
                .padding(vertical = 7.dp)
        )
        Box(
            Modifier
                .fillMaxWidth(0.62f)
                .clip(RoundedCornerShape(8.dp))
                .shimmer(shimmer)
                .background(Colors.SHIMMER)
                .padding(vertical = 7.dp)
        )
    }
}
