package de.fantjastisch.cards_frontend.card.create

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import de.fantjastisch.cards_frontend.card.update.TextFieldState
import de.fantjastisch.cards_frontend.card.update_and_create.CardEdit


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

    val navigator = LocalNavigator.currentOrThrow
    // einmaliger Effekt
    LaunchedEffect(
        // wenn sich diese Variable ändert
        key1 = viewModel.isFinished.value,
        // dann wird dieses Lambda ausgeführt.
        block = {
            if (viewModel.isFinished.value) {
                navigator.pop()
            }
        })
}
