package ru.topbun.upload.components

import android.R.attr.onClick
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import ru.topbun.core.ui.R
import ru.topbun.core.ui.components.AppButton
import ru.topbun.core.ui.components.DialogWrapper
import ru.topbun.core.ui.components.Height
import ru.topbun.core.ui.theme.Colors
import ru.topbun.core.ui.theme.Typography

@Composable
internal fun SuccessPublishRecipeDialog(
    onDismissRequest: () -> Unit,
    onClickOpenRecipe: () -> Unit,
) = DialogWrapper(onDismissRequest){
    Column(
        modifier = Modifier.fillMaxWidth()
            .padding(vertical = 48.dp, horizontal = 42.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            modifier = Modifier.size(160.dp),
            painter = painterResource(R.drawable.img_success_publish),
            contentDescription = null
        )
        Height(32.dp)
        Text(
            text = "Загрузка успешна",
            style = ru.topbun.core.ui.theme.Typography.H1,
            color = Colors.MAIN_TEXT,
            textAlign = TextAlign.Center
        )
        Height(8.dp)
        Text(
            text = "Ваш рецепт загружен, вы можете посмотреть его в своём профиле",
            style = Typography.P2,
            color = Colors.MAIN_TEXT,
            textAlign = TextAlign.Center
        )
        Height(24.dp)
        AppButton(
            modifier = Modifier.fillMaxWidth().heightIn(min = 56.dp),
            text = "Открыть рецепт",
            onClick = onClickOpenRecipe
        )
    }
}