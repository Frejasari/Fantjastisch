package de.fantjastisch.cards_frontend.category.update


import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import de.fantjastisch.cards_frontend.card.update.TextFieldState
import de.fantjastisch.cards_frontend.category.update_and_create.CategoryEdit
import de.fantjastisch.cards_frontend.infrastructure.effects.CloseScreenOnSignalEffect
import de.fantjastisch.cards_frontend.infrastructure.effects.ShowErrorOnSignalEffect
import java.util.*

/**
 * Rendert die Seite "Kategorie bearbeiten".
 *
 * @param modifier Modifier für die Seite.
 * @param id Id, der Kategorie, welche bearbeitet wird.
 *
 * @author Tamari Bayer
 */
@Composable
fun UpdateCategoryView(
    modifier: Modifier = Modifier,
    id: UUID
) {

    val viewModel = viewModel { UpdateCategoryViewModel(id = id) }
    CloseScreenOnSignalEffect(shouldClose = viewModel.isFinished.value)
    ShowErrorOnSignalEffect(viewModel = viewModel)

    CategoryEdit(
        modifier = modifier,
        label = TextFieldState(
            value = viewModel.label.value,
            errors = viewModel.errors.value,
            onValueChange = viewModel::setLabel,
        ),
        categories = viewModel.allCategories.value,
        onCategorySelected = viewModel::onCategorySelected,
        onUpdateCategoryClicked = viewModel::onUpdateCategoryClicked,
    )

}

