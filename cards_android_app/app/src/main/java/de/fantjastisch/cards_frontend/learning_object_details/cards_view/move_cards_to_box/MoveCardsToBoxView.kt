package de.fantjastisch.cards_frontend.learning_object_details.cards_view.move_cards_to_box

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import de.fantjastisch.cards_frontend.card.CardSelect
import de.fantjastisch.cards_frontend.infrastructure.CloseScreenOnSignalEffect


@Composable
fun MoveCardsToBoxView(
    modifier: Modifier = Modifier,
    viewModel: MoveCardsToBoxViewModel
) {
    LaunchedEffect(
        // wenn sich diese Variable ändert
        key1 = Unit,
        // dann wird dieses Lambda ausgeführt.
        block = {
            viewModel.onPageLoaded()
        })

    LazyColumn(
        modifier = modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(10.dp),
        contentPadding = PaddingValues(all = 16.dp)
    ) {
        CardSelect(
            cards = viewModel.cards.value,
            onCardSelected = viewModel::onCardSelected
        )
        item {
            Column() {
                if (viewModel.cards.value.size == 0) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center
                    ) { Text(text = "Looks empty...", fontSize = 20.sp) }
                } else {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceAround
                    ) {
                        FilledTonalButton(
                            onClick = viewModel::onMoveToPreviousBox,
                            enabled = !viewModel.isFirstBox
                        ) {
                            Text(
                                text =
                                if (viewModel.isFirstBox) {
                                    "                 "
                                } else {
                                    "Vorherige Box (" + (viewModel.learningBoxNum.value - 1) + ")"
                                }
                            )
                        }
                        FilledTonalButton(
                            onClick = viewModel::onMoveToNextBox, //viewModel::onAddCardsClicked
                            enabled = !viewModel.isLastBox
                        ) {
                            Text(
                                text =
                                if (viewModel.isLastBox) {
                                    "               "
                                } else {
                                    "Nächste Box (" + (viewModel.learningBoxNum.value + 1) + ")"
                                }
                            )
                        }
                    }
                }
            }

        }
    }

    CloseScreenOnSignalEffect(shouldClose = viewModel.isFinished.value)
}



