package ru.topbun.profile.components

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
import ru.topbun.core.ui.components.Height
import ru.topbun.core.ui.theme.Colors

@Composable
internal fun ProfileShimmer(
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .padding(horizontal = 12.dp).padding(top = 20.dp)
            .fillMaxWidth()
            .clip(RoundedCornerShape(44.dp))
            .background(Colors.WHITE)
            .padding(horizontal = 24.dp, vertical = 20.dp)
            .shimmer(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(96.dp)
                .clip(CircleShape)
                .background(Colors.SHIMMER)
        )
        Height(12.dp)
        Box(
            modifier = Modifier
                .size(width = 160.dp, height = 24.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(Colors.SHIMMER)
        )
        Height(8.dp)
        Box(
            modifier = Modifier
                .size(width = 200.dp, height = 14.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(Colors.SHIMMER)
        )
        Height(20.dp)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            repeat(3) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Box(
                        modifier = Modifier
                            .size(width = 40.dp, height = 22.dp)
                            .clip(RoundedCornerShape(6.dp))
                            .background(Colors.SHIMMER)
                    )
                    Height(6.dp)
                    Box(
                        modifier = Modifier
                            .size(width = 60.dp, height = 12.dp)
                            .clip(RoundedCornerShape(6.dp))
                            .background(Colors.SHIMMER)
                    )
                }
            }
        }
    }
}
