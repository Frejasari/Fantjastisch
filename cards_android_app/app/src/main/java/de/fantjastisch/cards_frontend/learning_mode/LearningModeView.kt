package de.fantjastisch.cards_frontend.card

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
import cafe.adriel.voyager.navigator.LocalNavigator
import de.fantjastisch.cards.R
import de.fantjastisch.cards_frontend.learning_mode.LearningModeCardComponent
import de.fantjastisch.cards_frontend.learning_mode.LearningModeViewModel


@Composable
fun LearningModeView(
    modifier: Modifier = Modifier,
    viewModel: LearningModeViewModel
) {
    LaunchedEffect(
        // wenn sich diese Variable ändert
        key1 = viewModel.currentCard.value,
        // dann wird dieses Lambda ausgeführt.
        block = {
            viewModel.onPageLoaded()
        })

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
                text = "Lernbox Nr. " + (learningBox.boxNumber.plus(1)).toString()
                        + " - " + learningBox.label,
                style = MaterialTheme.typography.titleMedium
            )
        } else {
            CircularProgressIndicator()
        }

        Text(
            text = "Anzahl Karten verbleibend: " + viewModel.numberOfCardsRemaining.toString(),
            style = MaterialTheme.typography.titleMedium,
            fontSize = 14.sp,
            fontWeight = FontWeight(350)
        )
        Divider(
            Modifier.padding(horizontal = 10.dp, vertical = 10.dp)
        )
        LearningModeCardComponent(
            content =
            if (viewModel.currentCard.value != null) {
                if (viewModel.isShowingAnswer.value) {
                    viewModel.currentCard.value!!.answer
                } else {
                    viewModel.currentCard.value!!.question
                }
            } else {
                "Lernbox leer"
            }
        )
        Divider(
            Modifier.padding(horizontal = 10.dp, vertical = 10.dp)
        )
        FilledTonalButton(
            modifier = Modifier
                .align(Alignment.CenterHorizontally),
            onClick = viewModel::onFlipCardClicked,
            enabled = viewModel.cardsInBox.value.isNotEmpty()
        ) {
            Text(
                text = if (viewModel.isShowingAnswer.value) {
                    stringResource(R.string.show_question_from_card_button)
                } else {
                    stringResource(R.string.show_answer_from_card_button)
                }
            )
        }
        Spacer(
            modifier = modifier
                .weight(1f)
        )
        if (!viewModel.isLastBox) {
            FilledTonalButton(
                modifier = Modifier.align(Alignment.CenterHorizontally),
                onClick = viewModel::onCardGoesToNextBoxClicked,
                enabled = viewModel.cardsInBox.value.isNotEmpty()
            ) {
                Text(text = "Karte in nächste Lernbox schieben")
            }
            FilledTonalButton(
                modifier = Modifier.align(Alignment.CenterHorizontally),
                onClick = viewModel::onCardStaysInBoxClicked,
                enabled = viewModel.cardsInBox.value.isNotEmpty()
            ) {
                Text(text = "Karte nicht weiterschieben")
            }
        } else {
            FilledTonalButton(
                modifier = Modifier.align(Alignment.CenterHorizontally),
                onClick = viewModel::onCardStaysInBoxClicked,
                enabled = viewModel.cardsInBox.value.isNotEmpty()
            ) {
                Text(text = "Nächste Karte")
            }
        }
    }
    val navigator = LocalNavigator.current!!
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




