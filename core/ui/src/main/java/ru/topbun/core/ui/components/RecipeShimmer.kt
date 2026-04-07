package ru.topbun.core.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.valentinilk.shimmer.ShimmerBounds
import com.valentinilk.shimmer.rememberShimmer
import com.valentinilk.shimmer.shimmer
import ru.topbun.core.ui.theme.Colors


@Composable
fun RecipeShimmer() {
    val shimmer = rememberShimmer(shimmerBounds = ShimmerBounds.Window)
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(32.dp))
            .background(Colors.WHITE)
            .padding(8.dp),
        horizontalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        Box(
            modifier = Modifier
                .size(100.dp)
                .clip(RoundedCornerShape(24.dp))
                .shimmer(shimmer)
                .background(Colors.SHIMMER),
        )
        Column(
            Modifier.padding(vertical = 6.dp)
        ) {
            Box(
                Modifier.fillMaxWidth()
                    .height(28.dp)
                    .shimmer(shimmer)
                    .background(Colors.SHIMMER),
            )
            Height(10.dp)
            Box(
                Modifier.fillMaxWidth()
                    .height(18.dp)
                    .shimmer(shimmer)
                    .background(Colors.SHIMMER),
            )
            Height(5.dp)
            Box(
                Modifier.fillMaxWidth()
                    .height(18.dp)
                    .shimmer(shimmer)
                    .background(Colors.SHIMMER),
            )
        }
    }
}