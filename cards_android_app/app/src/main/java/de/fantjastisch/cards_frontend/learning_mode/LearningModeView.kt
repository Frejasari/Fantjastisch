package de.fantjastisch.cards_frontend.learning_mode

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import de.fantjastisch.cards.R
import de.fantjastisch.cards_frontend.infrastructure.FantMainNavigator

@Composable
private fun showLoadingIcon() {
    Column(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.background)
            .padding(20.dp)
            .fillMaxSize(),
        verticalArrangement = Arrangement.Center
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            CircularProgressIndicator(modifier = Modifier.then(Modifier.size(60.dp)))
        }
    }
}

@Composable
fun LearningModeView(
    modifier: Modifier = Modifier,
    viewModel: LearningModeViewModel
) {
    val navigator = FantMainNavigator.current
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

    if (viewModel.isLoading.value) {
        showLoadingIcon()
    } else {
        Column(
            modifier = modifier
                .background(MaterialTheme.colorScheme.background)
                .padding(20.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {

            val learningBox = viewModel.learningBox.value
            if (learningBox != null) {
                Text(
                    text = String.format("%s Nr. %d - %s", stringResource(R.string.learningbox_label), learningBox.boxNumber.plus(1), learningBox.label),
                    style = MaterialTheme.typography.titleMedium
                )
            } else {
                showLoadingIcon()
            }

            Text(
                text = stringResource(R.string.remaining_cards_in_box_label) + viewModel.numberOfCardsRemaining.value.toString(),
                style = MaterialTheme.typography.titleMedium,
                fontSize = 14.sp,
                fontWeight = FontWeight(350)
            )
            Divider(
                Modifier.padding(horizontal = 10.dp, vertical = 10.dp)
            )
            if (viewModel.currentCard.value == null) {
                showLoadingIcon()
            } else {
                LearningModeCardComponent(
                    content = if (viewModel.isShowingAnswer.value) {
                        viewModel.currentCard.value!!.answer
                    } else {
                        viewModel.currentCard.value!!.question
                    },
                    onClick = viewModel::onFlipCardClicked,
                )
            }
            Divider(
                Modifier.padding(horizontal = 10.dp, vertical = 10.dp)
            )

            Spacer(
                modifier = modifier
                    .weight(1f)
            )
            if (!viewModel.isFirstBox) {
                FilledTonalButton(
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                    onClick = viewModel::onCardGoesToPreviousBoxClicked,
                    enabled = true
                ) {
                    Text(text = stringResource(R.string.move_card_to_previous_box_label))
                }
            }
            if (!viewModel.isLastBox) {
                FilledTonalButton(
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                    onClick = viewModel::onCardGoesToNextBoxClicked,
                    enabled = true
                ) {
                    Text(text = stringResource(R.string.move_card_to_next_box_label))
                }
                FilledTonalButton(
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                    onClick = viewModel::onCardStaysInBoxClicked,
                    enabled = true
                ) {
                    Text(text = stringResource(R.string.dont_move_card_text))
                }
            } else {
                FilledTonalButton(
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                    onClick = viewModel::onCardStaysInBoxClicked,
                    enabled = true
                ) {
                    Text(text = stringResource(R.string.next_card_text))
                }
            }
        }
    }
}




