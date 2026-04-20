package ru.topbun.upload.fragments

import android.R.attr.x
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import org.koin.compose.viewmodel.koinViewModel
import ru.topbun.core.ui.R
import ru.topbun.core.ui.components.AppButton
import ru.topbun.core.ui.theme.Colors
import ru.topbun.upload.UploadIntent
import ru.topbun.upload.UploadViewModel
import ru.topbun.upload.components.AddIngredientDialog
import ru.topbun.upload.components.AddStepDialog
import ru.topbun.upload.components.IngredientsSection
import ru.topbun.upload.components.StepsSection

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

    StepsSection(
        steps = state.steps,
        onClickAddStep = {
            viewModel.sendIntent(UploadIntent.ChangeShowDialogAddStep(true))
        },
        onClickRemoveStep = {
            viewModel.sendIntent(UploadIntent.RemoveStep(it))
        },
        onReorderSteps = { fromIndex, toIndex ->
            viewModel.sendIntent(UploadIntent.ReorderStep(fromIndex, toIndex))
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

    if (state.showDialogAddStep) {
        AddStepDialog(
            onDismissRequest = {
                viewModel.sendIntent(UploadIntent.ChangeShowDialogAddStep(false))
            },
            onClickConfirm = { description, previewUri ->
                viewModel.sendIntent(UploadIntent.AddStep(description, previewUri))
            }
        )
    }
}
