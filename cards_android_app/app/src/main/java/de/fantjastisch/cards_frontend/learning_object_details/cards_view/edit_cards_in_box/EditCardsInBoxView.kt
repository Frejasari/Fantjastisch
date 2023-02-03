package de.fantjastisch.cards_frontend.learning_object_details.cards_view.edit_cards_in_box

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import de.fantjastisch.cards_frontend.card.CardSelect
import de.fantjastisch.cards_frontend.components.SaveLayout
import de.fantjastisch.cards_frontend.infrastructure.CloseScreenOnSignalEffect
import de.fantjastisch.cards_frontend.util.LoadingIcon
import de.fantjastisch.cards_frontend.util.LoadingWrapper

/**
 * Rendert die Seite Karten in dieser Box.
 *
 * @param modifier Modifier für die Seite.
 * @param viewModel Die ViewModel, die die Daten zur Verfügung stellt.
 *
 * @author
 */
@Composable
fun EditCardsInBoxView(
    modifier: Modifier = Modifier,
    viewModel: EditCardsInBoxViewModel
) {
    LaunchedEffect(
        // wenn sich diese Variable ändert
        key1 = Unit,
        // dann wird dieses Lambda ausgeführt.
        block = {
            viewModel.onPageLoaded()
        })

    LoadingWrapper(isLoading=viewModel.isLoading.value) {
        SaveLayout(onSaveClicked = viewModel::onAddCardsClicked, modifier = modifier) {
            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(10.dp),
            ) {
                CardSelect(
                    cards = viewModel.cards.value,
                    onCardSelected = viewModel::onCardSelected
                )
            }
        }
    }

    CloseScreenOnSignalEffect(shouldClose = viewModel.isFinished.value)

}


