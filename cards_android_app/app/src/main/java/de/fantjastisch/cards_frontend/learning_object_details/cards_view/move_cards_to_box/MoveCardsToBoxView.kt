package de.fantjastisch.cards_frontend.learning_object_details.cards_view.move_cards_to_box

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import de.fantjastisch.cards.R
import de.fantjastisch.cards_frontend.card.CardSelect
import de.fantjastisch.cards_frontend.infrastructure.CloseScreenOnSignalEffect
import de.fantjastisch.cards_frontend.util.LoadingIcon


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

    Column() {
        LazyColumn(
            modifier = modifier
                .fillMaxWidth()
                .weight(1f),
            verticalArrangement = Arrangement.spacedBy(10.dp),
            contentPadding = PaddingValues(all = 16.dp)
        ) {
            CardSelect(
                cards = viewModel.cards.value,
                onCardSelected = viewModel::onCardSelected
            )
            item {
                Column {
                    if (viewModel.isLoading.value) {
                        LoadingIcon()
                    } else if (viewModel.cards.value.isEmpty()) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Text(
                                text = stringResource(R.string.no_content_text)
                            )
                        }
                    }
                }
            }

        }
        if (viewModel.cards.value.isNotEmpty()) {
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
                            String.format(
                                "%s (%d)",
                                stringResource(R.string.previous_box_text),
                                (viewModel.learningBoxNum.value - 1)
                            )
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
                            String.format(
                                "%s (%d)",
                                stringResource(R.string.next_box_text),
                                (viewModel.learningBoxNum.value + 1)
                            )
                        }
                    )
                }
            }
        }
    }

    CloseScreenOnSignalEffect(shouldClose = viewModel.isFinished.value)
}



