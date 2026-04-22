package ru.topbun.assistant.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
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
import ru.topbun.core.ui.theme.Colors

@Composable
internal fun ChatLoadingList(
    contentPadding: PaddingValues,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        contentPadding = contentPadding
    ) {
        items(6) { index ->
            ChatLoadingBubble(isUser = index % 3 == 1)
        }
    }
}

@Composable
private fun ChatLoadingBubble(isUser: Boolean) {
    val shimmer = rememberShimmer(shimmerBounds = ShimmerBounds.Window)

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = if (isUser) Arrangement.End else Arrangement.Start
    ) {
        Column(
            modifier = Modifier
                .widthIn(max = 0.78f * LocalConfiguration.current.screenWidthDp.dp)
                .clip(
                    RoundedCornerShape(
                        topStart = 26.dp,
                        topEnd = 26.dp,
                        bottomStart = if (isUser) 26.dp else 10.dp,
                        bottomEnd = if (isUser) 10.dp else 26.dp
                    )
                )
                .background(if (isUser) Colors.PRIMARY.copy(alpha = 0.16f) else Colors.WHITE)
                .padding(horizontal = 18.dp, vertical = 16.dp),
            horizontalAlignment = if (isUser) Alignment.End else Alignment.Start
        ) {
            Box(
                Modifier
                    .fillMaxWidth(if (isUser) 0.7f else 0.92f)
                    .height(16.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .shimmer(shimmer)
                    .background(Colors.SHIMMER)
            )
            Box(
                Modifier
                    .padding(top = 8.dp)
                    .fillMaxWidth(if (isUser) 0.48f else 0.66f)
                    .height(16.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .shimmer(shimmer)
                    .background(Colors.SHIMMER)
            )
        }
    }
}
