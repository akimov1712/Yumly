package ru.topbun.profile_followers.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.valentinilk.shimmer.shimmer
import ru.topbun.core.ui.theme.Colors

@Composable
internal fun UserShimmer(
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(28.dp))
            .background(Colors.WHITE)
            .padding(horizontal = 16.dp, vertical = 12.dp)
            .shimmer(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        Box(
            modifier = Modifier
                .size(52.dp)
                .clip(CircleShape)
                .background(Colors.SHIMMER)
        )
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(width = 140.dp, height = 16.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(Colors.SHIMMER)
            )
            Box(
                modifier = Modifier
                    .size(width = 200.dp, height = 12.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(Colors.SHIMMER)
            )
        }
    }
}
