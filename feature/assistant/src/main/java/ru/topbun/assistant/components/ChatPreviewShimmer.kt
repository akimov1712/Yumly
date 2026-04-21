package ru.topbun.assistant.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.valentinilk.shimmer.ShimmerBounds
import com.valentinilk.shimmer.rememberShimmer
import com.valentinilk.shimmer.shimmer
import ru.topbun.core.ui.components.Height
import ru.topbun.core.ui.theme.Colors

@Composable
internal fun ChatPreviewShimmer() {
    val shimmer = rememberShimmer(shimmerBounds = ShimmerBounds.Window)
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(28.dp))
            .background(Colors.FORM)
            .padding(16.dp)
    ) {
        Box(
            Modifier
                .fillMaxWidth(0.52f)
                .height(22.dp)
                .clip(RoundedCornerShape(10.dp))
                .shimmer(shimmer)
                .background(Colors.SHIMMER)
        )
        Height(12.dp)
        Box(
            Modifier
                .fillMaxWidth()
                .height(18.dp)
                .clip(RoundedCornerShape(9.dp))
                .shimmer(shimmer)
                .background(Colors.SHIMMER)
        )
        Height(7.dp)
        Box(
            Modifier
                .fillMaxWidth(0.74f)
                .height(18.dp)
                .clip(RoundedCornerShape(9.dp))
                .shimmer(shimmer)
                .background(Colors.SHIMMER)
        )
    }
}
