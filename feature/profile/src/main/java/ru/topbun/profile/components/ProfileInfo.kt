package ru.topbun.profile.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import ru.topbun.core.ui.R
import ru.topbun.core.ui.components.AppAsyncImage
import ru.topbun.core.ui.components.AppButton
import ru.topbun.core.ui.components.AppOutlinedButton
import ru.topbun.core.ui.components.Height
import ru.topbun.core.ui.theme.Colors
import ru.topbun.core.ui.theme.Typography
import ru.topbun.domain.entity.account.ProfileEntity

@Composable
internal fun ProfileInfo(
    profile: ProfileEntity?,
    isSelf: Boolean,
    countRecipes: Int,
    followLoading: Boolean,
    onClickFollow: () -> Unit,
    onClickFollowers: () -> Unit,
    onClickFollowing: () -> Unit,
) {
    Column(
        modifier = Modifier
            .padding(top = 20.dp)
            .fillMaxWidth()
            .clip(RoundedCornerShape(44.dp))
            .background(Colors.WHITE)
            .padding(horizontal = 24.dp, vertical = 20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Avatar(profile?.photoUrl)
        Height(12.dp)
        Text(
            text = profile?.username.orEmpty().ifBlank { " " },
            style = Typography.H1,
            color = Colors.MAIN_TEXT,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            textAlign = TextAlign.Center
        )
        val email = profile?.email
        if (isSelf && !email.isNullOrBlank()) {
            Height(4.dp)
            Text(
                text = email,
                style = Typography.S,
                color = Colors.SECONDARY_TEXT,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                textAlign = TextAlign.Center
            )
        }
        Height(20.dp)
        Stats(
            countRecipes = countRecipes,
            countFollowers = profile?.countFollowers ?: 0,
            countFollowing = profile?.countFollowing ?: 0,
            onClickFollowers = onClickFollowers,
            onClickFollowing = onClickFollowing,
        )
        if (!isSelf && profile != null) {
            Height(20.dp)
            FollowButton(
                isFollow = profile.isFollow,
                isLoading = followLoading,
                onClick = onClickFollow
            )
        }
    }
}

@Composable
private fun Avatar(url: String?) {
    Box(
        modifier = Modifier
            .size(96.dp)
            .clip(CircleShape)
            .background(Colors.FORM)
            .border(2.dp, Colors.OUTLINE.copy(0.5f), CircleShape),
        contentAlignment = Alignment.Center
    ) {
        if (url.isNullOrBlank()) {
            Icon(
                modifier = Modifier.fillMaxSize().padding(20.dp),
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

@Composable
private fun Stats(
    countRecipes: Int,
    countFollowers: Int,
    countFollowing: Int,
    onClickFollowers: () -> Unit,
    onClickFollowing: () -> Unit,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        StatItem(value = countRecipes, label = "Рецепты")
        StatDivider()
        StatItem(value = countFollowers, label = "Подписчики", onClick = onClickFollowers)
        StatDivider()
        StatItem(value = countFollowing, label = "Подписки", onClick = onClickFollowing)
    }
}

@Composable
private fun StatDivider() {
    Box(
        modifier = Modifier
            .size(1.dp, 24.dp)
            .background(Colors.OUTLINE)
    )
}

@Composable
private fun FollowButton(
    isFollow: Boolean,
    isLoading: Boolean,
    onClick: () -> Unit
) {
    if (isFollow) {
        AppOutlinedButton(
            text = "Отписаться",
            modifier = Modifier
                .fillMaxWidth()
                .defaultMinSize(minHeight = 48.dp),
            isLoading = isLoading,
            borderColor = Colors.OUTLINE,
            contentColor = Colors.BLUE_TEXT,
            onClick = onClick
        )
    } else {
        AppButton(
            text = "Подписаться",
            modifier = Modifier
                .fillMaxWidth()
                .defaultMinSize(minHeight = 48.dp),
            isLoading = isLoading,
            onClick = onClick
        )
    }
}
