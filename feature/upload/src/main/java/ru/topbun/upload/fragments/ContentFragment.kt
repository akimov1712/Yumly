package ru.topbun.upload.fragments

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.koin.compose.viewmodel.koinViewModel
import ru.topbun.core.ui.components.AppOutlinedButton
import ru.topbun.upload.UploadIntent
import ru.topbun.upload.UploadViewModel
import ru.topbun.upload.components.AddIngredientDialog
import ru.topbun.upload.components.IngredientsSection

@Composable
internal fun ContentFragment(
    viewModel: UploadViewModel = koinViewModel()
) = Column(
    verticalArrangement = Arrangement.spacedBy(20.dp)
){
    val state by viewModel.state.collectAsState()

    IngredientsSection(
        ingredients = state.ingredients,
        onClickAddIngredient = {
            viewModel.sendIntent(UploadIntent.ChangeShowDialogAddIngredient(true))
        },
        onClickRemoveIngredient = {
            viewModel.sendIntent(UploadIntent.RemoveIngredient(it))
        },
        onReorderIngredients = { fromIndex, toIndex ->
            viewModel.sendIntent(UploadIntent.ReorderIngredient(fromIndex, toIndex))
        }
    )

    if (state.showDialogAddIngredient) {
        AddIngredientDialog(
            onDismissRequest = {
                viewModel.sendIntent(UploadIntent.ChangeShowDialogAddIngredient(false))
            },
            onClickConfirm = { name, value ->
                viewModel.sendIntent(UploadIntent.AddIngredient(name, value))
            }
        )
    }
}
