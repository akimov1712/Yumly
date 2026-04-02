package ru.topbun.core.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import ru.topbun.core.ui.R
import ru.topbun.core.ui.theme.Colors
import ru.topbun.core.ui.theme.Typography
import ru.topbun.core.ui.utils.LocalBottomBarPadding

@Composable
fun ColumnScope.RecipeList() {
    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .weight(1f),
        verticalArrangement = Arrangement.spacedBy(20.dp),
        contentPadding = PaddingValues(start = 12.dp, end = 12.dp, top = 15.dp, bottom = LocalBottomBarPadding.current)
    ) {
        items(10){
            RecipeItem()
        }
    }
}

@Composable
private fun RecipeItem(){
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(32.dp))
            .background(Colors.WHITE)
            .padding(8.dp),
        horizontalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        Preview("https://images.gastronom.ru/LUCWGGJ_W0cOOP-8i2Zty8b3kU3HRVBY10up-ARkRqs/pr:article-content-image/g:ce/rs:auto:0:0:0/L2Ntcy9hbGwtaW1hZ2VzLzI1Zjg1NmNhLTc3YzAtNGFhMS04Y2JjLWYzMTNmZjAwODE0Zi5qcGc.webp")
        Information()
    }
}

@Composable
private fun Information() {
    Column(
        Modifier.padding(vertical = 6.dp)
    ){
        Title()
        Height(10.dp)
        ChipList()
    }
}

@Composable
private fun Title() {
    Text(
        text = buildAnnotatedString {
            append("Pancake")
            withStyle(SpanStyle(color = Colors.PRIMARY)){
                append(" (Easy)")
            }
        },
        style = Typography.H2,
        color = Colors.BLUE_TEXT
    )
}

@Composable
private fun ChipList() {
    Column(
        verticalArrangement = Arrangement.spacedBy(5.dp)
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Chip(
                icon = painterResource(R.drawable.ic_calories),
                title = "120 kcal"
            )
            Box(Modifier
                .size(4.dp)
                .background(Colors.SECONDARY_TEXT, CircleShape))
            Chip(
                icon = painterResource(R.drawable.ic_time),
                title = "20 min"
            )
        }
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Chip(
                icon = painterResource(R.drawable.ic_ingredients),
                title = "5 Ingredients"
            )
            Box(Modifier
                .size(4.dp)
                .background(Colors.SECONDARY_TEXT, CircleShape))
            Chip(
                icon = painterResource(R.drawable.ic_steps),
                title = "7 Steps"
            )
        }
    }
}

@Composable
private fun Chip(
    icon: Painter,
    title: String
) {
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painter = icon,
            modifier = Modifier.size(12.dp),
            contentDescription = title,
            tint = Colors.SECONDARY_TEXT
        )
        Width(4.dp)
        Text(
            text = title,
            color = Colors.SECONDARY_TEXT,
            style = Typography.S
        )
    }
}

@Composable
private fun Preview(url: String) {
    AppAsyncImage(
        url = url,
        modifier = Modifier
            .size(100.dp)
            .clip(RoundedCornerShape(24.dp)),
        contentScale = ContentScale.Crop
    )
}

