package de.fantjastisch.cards_frontend.card.create


import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import de.fantjastisch.cards.R
import de.fantjastisch.cards_frontend.card.update.TextFieldState
import de.fantjastisch.cards_frontend.card.update_and_create.CardEdit
import de.fantjastisch.cards_frontend.infrastructure.CloseScreenOnSignalEffect


//TODO Fehler anzeigen.
@Composable
fun CreateCardView(
    modifier: Modifier = Modifier
) {

    val viewModel = viewModel { CreateCardViewModel() }

    CardEdit(
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
        onDeleteLinkClicked = viewModel::onDeleteLinkClicked
    )

    FilledTonalButton(
        onClick = viewModel::onCreateCardClicked
    ) {
        Text(text = stringResource(R.string.save_button_text))
    }


    CloseScreenOnSignalEffect(shouldClose = viewModel.isFinished.value)
}
