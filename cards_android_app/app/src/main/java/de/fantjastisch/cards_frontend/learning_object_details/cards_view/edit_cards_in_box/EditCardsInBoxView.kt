package de.fantjastisch.cards_frontend.learning_object_details.cards_view.edit_cards_in_box

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import de.fantjastisch.cards.R
import de.fantjastisch.cards_frontend.card.CardSelect
import de.fantjastisch.cards_frontend.infrastructure.CloseScreenOnSignalEffect


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

    Column() {
        LazyColumn(
            modifier = modifier,
            verticalArrangement = Arrangement.spacedBy(10.dp),
            contentPadding = PaddingValues(all = 16.dp)
        ) {
            CardSelect(
                cards = viewModel.cards.value,
                onCardSelected = viewModel::onCardSelected
            )
        }

        FilledTonalButton(
            modifier = Modifier.align(Alignment.CenterHorizontally),
            onClick = viewModel::onAddCardsClicked
        ) {
            Text(text = stringResource(R.string.save_button_text))
        }
    }


    CloseScreenOnSignalEffect(shouldClose = viewModel.isFinished.value)

}


