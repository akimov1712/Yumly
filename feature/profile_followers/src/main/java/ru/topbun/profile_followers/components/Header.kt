package ru.topbun.profile_followers.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.dropShadow
import androidx.compose.ui.graphics.shadow.Shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import ru.topbun.core.ui.R
import ru.topbun.core.ui.components.AppAsyncImage
import ru.topbun.core.ui.theme.Colors
import ru.topbun.core.ui.theme.Typography
import ru.topbun.domain.entity.account.ProfileEntity

@Composable
internal fun Header(
    profile: ProfileEntity?,
    onClickBack: () -> Unit,
) {
    Row(
        modifier = Modifier
            .padding(horizontal = 12.dp)
            .padding(top = 20.dp)
            .fillMaxWidth()
            .clip(RoundedCornerShape(44.dp))
            .background(Colors.WHITE)
            .padding(10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(
            modifier = Modifier
                .size(48.dp)
                .dropShadow(
                    shape = CircleShape,
                    shadow = Shadow(radius = 4.dp, alpha = 0.1f)
                )
                .clip(CircleShape)
                .background(Colors.WHITE),
            onClick = onClickBack
        ) {
            Icon(
                modifier = Modifier.size(24.dp),
                painter = painterResource(R.drawable.ic_back),
                contentDescription = null,
                tint = Colors.BLUE_TEXT
            )
        }
        Box(
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 8.dp),
            contentAlignment = Alignment.Center
        ) {
            if (profile != null) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Avatar(profile.photoUrl)
                    Text(
                        text = profile.username,
                        style = Typography.H2,
                        color = Colors.MAIN_TEXT,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
        }
        Spacer(Modifier.size(48.dp))
    }
}

@Composable
private fun Avatar(url: String?) {
    Box(
        modifier = Modifier
            .size(32.dp)
            .clip(CircleShape)
            .background(Colors.FORM)
            .border(1.dp, Colors.OUTLINE.copy(0.5f), CircleShape),
        contentAlignment = Alignment.Center
    ) {
        if (url.isNullOrBlank()) {
            Icon(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(4.dp),
                painter = painterResource(R.drawable.ic_user),
                contentDescription = null,
                tint = Colors.SECONDARY_TEXT
            )
        } else {
            AppAsyncImage(
                modifier = Modifier.fillMaxSize(),
                url = url,
                contentScale = ContentScale.Crop
            )
        }
    }
}
