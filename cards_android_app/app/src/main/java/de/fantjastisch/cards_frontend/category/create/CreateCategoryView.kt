package de.fantjastisch.cards_frontend.category.create


import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import de.fantjastisch.cards_frontend.card.update.TextFieldState
import de.fantjastisch.cards_frontend.category.update_and_create.CategoryEdit
import de.fantjastisch.cards_frontend.infrastructure.CloseScreenOnSignalEffect


//TODO Fehler anzeigen.
@Composable
fun CreateCategoryView(
    modifier: Modifier = Modifier
) {

    val viewModel = viewModel { CreateCategoryViewModel() }

    CategoryEdit(
        modifier = modifier,
        label = TextFieldState(
            value = viewModel.label.value,
            errors = viewModel.errors.value,
            onValueChange = viewModel::setLabel,
        ),
        categories = viewModel.allCats.value,
        onCategorySelected = viewModel::onCategorySelected,
        onUpdateCategoryClicked = viewModel::onCreateCategoryClicked,
    )


    CloseScreenOnSignalEffect(shouldClose = viewModel.isFinished.value)
}
