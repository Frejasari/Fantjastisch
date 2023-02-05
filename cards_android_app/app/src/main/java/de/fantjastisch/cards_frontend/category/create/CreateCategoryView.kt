package de.fantjastisch.cards_frontend.category.create


import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import de.fantjastisch.cards_frontend.card.update.TextFieldState
import de.fantjastisch.cards_frontend.category.update_and_create.CategoryEditView
import de.fantjastisch.cards_frontend.infrastructure.effects.CloseScreenOnSignalEffect
import de.fantjastisch.cards_frontend.infrastructure.effects.ShowErrorOnSignalEffect


/**
 * Rendert die Seite "Kategorie erstellen".
 *
 * @param modifier Modifier für die Seite.
 *
 * @author Tamari Bayer, Semjon Nirmann
 */
@Composable
fun CreateCategoryView(
    modifier: Modifier = Modifier
) {

    val viewModel = viewModel { CreateCategoryViewModel() }

    CloseScreenOnSignalEffect(shouldClose = viewModel.isFinished.value)
    ShowErrorOnSignalEffect(viewModel)

    CategoryEditView(
        modifier = modifier,
        label = TextFieldState(
            value = viewModel.label.value,
            errors = viewModel.errors.value,
            onValueChange = viewModel::setLabel,
        ),
        categories = viewModel.allCategories.value,
        onCategorySelected = viewModel::onCategorySelected,
        onUpdateCategoryClicked = viewModel::onCreateCategoryClicked,
    )


}
