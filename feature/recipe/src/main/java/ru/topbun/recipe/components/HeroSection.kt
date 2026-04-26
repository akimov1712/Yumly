package ru.topbun.recipe.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.shadow.Shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import ru.topbun.core.ui.R
import ru.topbun.core.ui.components.AppAsyncImage
import ru.topbun.core.ui.components.Width
import ru.topbun.core.ui.theme.Colors
import ru.topbun.core.ui.theme.Typography
import ru.topbun.core.ui.utils.rippleClickable
import ru.topbun.domain.entity.account.ProfileEntity
import ru.topbun.domain.entity.recipe.RecipeEntity

@Composable
internal fun HeroSection(
    recipe: RecipeEntity,
    isFavorite: Boolean,
    favoriteLoading: Boolean,
    isOwnRecipe: Boolean,
    onClickBack: () -> Unit,
    onClickShare: () -> Unit,
    onClickFavorite: () -> Unit,
    onClickDelete: () -> Unit,
    onClickAuthor: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .aspectRatio(0.95f)
            .background(Colors.FORM)
    ) {
        AppAsyncImage(
            modifier = Modifier.fillMaxSize(),
            url = recipe.smallImage,
            contentScale = ContentScale.Crop
        )
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Colors.BLACK.copy(0.45f),
                            Colors.BLACK.copy(0.05f),
                            Colors.BLACK.copy(0.55f)
                        )
                    )
                )
        )
        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .padding(horizontal = 12.dp, vertical = 12.dp)
        ) {
            TopBar(
                isFavorite = isFavorite,
                favoriteLoading = favoriteLoading,
                isOwnRecipe = isOwnRecipe,
                onClickBack = onClickBack,
                onClickShare = onClickShare,
                onClickFavorite = onClickFavorite,
                onClickDelete = onClickDelete,
            )
            Spacer(Modifier.weight(1f))
            HeroContent(
                title = recipe.title,
                author = recipe.author,
                isOwnRecipe = isOwnRecipe,
                onClickAuthor = onClickAuthor
            )
        }
    }
}

@Composable
private fun TopBar(
    isFavorite: Boolean,
    favoriteLoading: Boolean,
    isOwnRecipe: Boolean,
    onClickBack: () -> Unit,
    onClickShare: () -> Unit,
    onClickFavorite: () -> Unit,
    onClickDelete: () -> Unit,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        CircleIconButton(
            iconRes = R.drawable.ic_back,
            onClick = onClickBack
        )
        Spacer(Modifier.weight(1f))
        if (isOwnRecipe) {
            CircleIconButton(
                iconRes = R.drawable.ic_close,
                tint = Colors.SECONDARY,
                onClick = onClickDelete
            )
            Width(8.dp)
        }
        CircleIconButton(
            iconRes = R.drawable.ic_share,
            onClick = onClickShare
        )
        Width(8.dp)
        CircleIconButton(
            iconRes = if (isFavorite) R.drawable.ic_heart_filled else R.drawable.ic_heart,
            tint = if (isFavorite) Colors.SECONDARY else Colors.MAIN_TEXT,
            enabled = !favoriteLoading,
            onClick = onClickFavorite
        )
    }
}

@Composable
private fun HeroContent(
    title: String,
    author: ProfileEntity,
    isOwnRecipe: Boolean,
    onClickAuthor: () -> Unit,
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = title,
            style = Typography.H1,
            color = Colors.WHITE,
            maxLines = 3,
            overflow = TextOverflow.Ellipsis
        )
        AuthorPill(
            author = author,
            isOwnRecipe = isOwnRecipe,
            onClick = onClickAuthor
        )
    }
}

@Composable
private fun AuthorPill(
    author: ProfileEntity,
    isOwnRecipe: Boolean,
    onClick: () -> Unit,
) {
    Row(
        modifier = Modifier
            .clip(RoundedCornerShape(44.dp))
            .background(Colors.WHITE.copy(0.95f))
            .rippleClickable(enabled = !isOwnRecipe, onClick = onClick)
            .padding(start = 6.dp, end = 16.dp, top = 6.dp, bottom = 6.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Box(
            modifier = Modifier
                .size(36.dp)
                .clip(CircleShape)
                .background(Colors.FORM)
                .border(1.dp, Colors.OUTLINE.copy(0.5f), CircleShape),
            contentAlignment = Alignment.Center
        ) {
            if (author.photoUrl.isNullOrBlank()) {
                Icon(
                    modifier = Modifier.fillMaxSize().padding(8.dp),
                    painter = painterResource(R.drawable.ic_user),
                    contentDescription = null,
                    tint = Colors.SECONDARY_TEXT
                )
            } else {
                AppAsyncImage(
                    modifier = Modifier.fillMaxSize(),
                    url = author.photoUrl,
                    contentScale = ContentScale.Crop
                )
            }
        }
        Column {
            Text(
                text = if (isOwnRecipe) "Это вы" else "Автор",
                style = Typography.S,
                color = if (isOwnRecipe) Colors.PRIMARY else Colors.SECONDARY_TEXT
            )
            Text(
                text = author.username,
                style = Typography.H3,
                color = Colors.MAIN_TEXT,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Composable
private fun CircleIconButton(
    iconRes: Int,
    onClick: () -> Unit,
    tint: androidx.compose.ui.graphics.Color = Colors.MAIN_TEXT,
    enabled: Boolean = true,
) {
    IconButton(
        modifier = Modifier
            .size(44.dp)
            .dropShadow(
                shape = CircleShape,
                shadow = Shadow(radius = 4.dp, alpha = 0.15f)
            )
            .clip(CircleShape)
            .background(Colors.WHITE),
        onClick = onClick,
        enabled = enabled
    ) {
        Icon(
            modifier = Modifier.size(20.dp),
            painter = painterResource(iconRes),
            contentDescription = null,
            tint = tint
        )
    }
}
