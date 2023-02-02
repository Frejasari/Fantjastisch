package de.fantjastisch.cards_frontend.card.create


import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import de.fantjastisch.cards_frontend.card.update.TextFieldState
import de.fantjastisch.cards_frontend.card.update_and_create.CardEditView
import de.fantjastisch.cards_frontend.infrastructure.CloseScreenOnSignalEffect

//TODO Fehler anzeigen.
/**
 * Rendert die Seite "Karteikarte erstellen".
 *
 * @author Freja Sender, Tamari Bayer
 *
 * @param modifier Modifier für die Seite.
 */
@Composable
fun CreateCardView(
    modifier: Modifier = Modifier
) {

    val viewModel = viewModel { CreateCardViewModel() }

    CardEditView(
        modifier = modifier,
        question = TextFieldState(
            value = viewModel.cardQuestion.value,
            errors = viewModel.errors.value,
            onValueChange = viewModel::setCardQuestion,
        ),
        answer = TextFieldState(
            value = viewModel.cardAnswer.value,
            errors = viewModel.errors.value,
            onValueChange = viewModel::setCardAnswer,
        ),
        tag = TextFieldState(
            value = viewModel.cardTag.value,
            errors = viewModel.errors.value,
            onValueChange = viewModel::setCardTag,
        ),
        categories = viewModel.cardCategories.value,
        onCategorySelected = viewModel::onCategorySelected,
        linkName = TextFieldState(
            value = viewModel.linkName.value,
            errors = viewModel.errors.value,
            onValueChange = viewModel::setLinkName,
        ),
        cards = viewModel.cards.value,
        onCardSelected = viewModel::onCardSelected,
        onUpdateCardClicked = viewModel::onCreateCardClicked,
        onCreateLinkClicked = viewModel::onCreateLinkClicked,
        links = viewModel.cardLinks.value,
        onDeleteLinkClicked = viewModel::onDeleteLinkClicked,
        toast = viewModel.toast.value,
        noCategories = viewModel.noCategories.value
    )


    CloseScreenOnSignalEffect(shouldClose = viewModel.isFinished.value)
}
