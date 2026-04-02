package ru.topbun.home.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.topbun.core.ui.theme.Colors
import ru.topbun.core.ui.theme.Fonts
import ru.topbun.core.ui.utils.rippleClickable
import ru.topbun.home.HomeState
import ru.topbun.home.HomeState.SearchType.All
import ru.topbun.home.HomeState.SearchType.Subscribers

@Composable
internal fun SearchTypeBar(
    types: List<HomeState.SearchType>,
    selectedIndex: Int,
    onChangeType: (index: Int) -> Unit
) {
    Row(
        modifier = Modifier.padding(horizontal = 12.dp)
            .fillMaxWidth()
            .clip(RoundedCornerShape(44.dp))
            .background(Colors.WHITE)
            .padding(5.dp),
        horizontalArrangement = Arrangement.spacedBy(5.dp)
    ) {
        types.forEachIndexed { index, type ->
            val selected = index == selectedIndex
            val textColor = if(selected) Colors.WHITE else Colors.SECONDARY_TEXT
            val bgColor = if(selected) Colors.PRIMARY else Color.Transparent
            val weight = when(type){
                All -> 1f
                Subscribers -> 1.5f
            }
            Box(
                modifier = Modifier.weight(weight)
                    .clip(RoundedCornerShape(44.dp))
                    .background(bgColor)
                    .rippleClickable(){ onChangeType(index) }
                    .padding(vertical = 12.dp),
                contentAlignment = Alignment.Center
            ){
                Text(
                    text = type.title,
                    color = textColor,
                    fontFamily = Fonts.INTER,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 15.sp,
                    lineHeight = 25.sp,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}