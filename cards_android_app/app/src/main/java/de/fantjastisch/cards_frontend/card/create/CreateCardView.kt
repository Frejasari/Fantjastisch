package de.fantjastisch.cards_frontend.card.create


import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
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
        onUpdateCardClicked = viewModel::onCreateCardClicked,
    )
    FilledTonalButton(
        onClick = viewModel::onCreateCardClicked
    ) {
        Text(text = stringResource(R.string.create_card_save_button_text))
    }


    CloseScreenOnSignalEffect(shouldClose = viewModel.isFinished.value)
}
