package de.fantjastisch.cards_frontend.learning_mode

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Divider
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import de.fantjastisch.cards.R
import de.fantjastisch.cards_frontend.components.TwoTextsWithDivider
import de.fantjastisch.cards_frontend.glossary.LinkWithoutDeleteComponent
import de.fantjastisch.cards_frontend.infrastructure.effects.CloseScreenOnSignalEffect
import de.fantjastisch.cards_frontend.infrastructure.effects.OnFirstLoadedSignalEffect
import de.fantjastisch.cards_frontend.infrastructure.effects.ShowErrorOnSignalEffect
import de.fantjastisch.cards_frontend.util.LoadingWrapper
import de.fantjastisch.cards_frontend.util.formatToInlineLabel
import java.util.*

/**
 * Rendert die Seite "Lernmodus".
 *
 * @param modifier Modifier für die Seite.
 * @param learningBoxId Id, der ausgewählten Lernbox.
 * @param learningObjectId Id, des ausgewählten Lernobjektes.
 * @param sort True, wenn Karten alphabetisch sortiert.
 *
 * @author Jessica Repty, Freja Sender, Semjon Nirmann
 */
@OptIn(ExperimentalLayoutApi::class)
@Composable
fun LearningModeView(
    modifier: Modifier = Modifier,
    learningBoxId: UUID,
    learningObjectId: UUID,
    sort: Boolean
) {

    val viewModel = viewModel {
        LearningModeViewModel(
            learningBoxId = learningBoxId,
            learningObjectId = learningObjectId,
            sort = sort
        )
    }
    CloseScreenOnSignalEffect(shouldClose = viewModel.isFinished.value)
    ShowErrorOnSignalEffect(viewModel = viewModel)
    OnFirstLoadedSignalEffect(onPageLoaded = viewModel::onPageLoaded)


    LoadingWrapper(isLoading = viewModel.isLoading.value) {
        Column(
            modifier = modifier
                .background(MaterialTheme.colorScheme.background)
                .padding(15.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Text(
                text = String.format(
                    "%s Nr. %d - %s",
                    stringResource(R.string.learningbox_label),
                    // wir wissen, dass dieses nicht mehr null ist, da loading abgeschlossen.
                    viewModel.learningBox.value!!.boxNumber.plus(1),
                    viewModel.learningBox.value!!.label
                ),
                style = MaterialTheme.typography.titleMedium
            )

            Text(
                text = stringResource(R.string.remaining_cards_in_box_text)
                    .formatToInlineLabel() + viewModel.numberOfCardsRemaining.value.toString(),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Medium
            )
            Divider(
                Modifier.padding(horizontal = 7.dp, vertical = 10.dp)
            )
            TwoTextsWithDivider(
                headline = stringResource(R.string.tag_label),
                text = viewModel.currentCard.value!!.tag
            )
            LearningModeCardComponent(
                content = if (viewModel.isShowingAnswer.value) {
                    viewModel.currentCard.value!!.answer
                } else {
                    viewModel.currentCard.value!!.question
                },
                onClick = viewModel::onFlipCardClicked,
            )
            Column(
                modifier = Modifier
                    .weight(1f)
                    .verticalScroll(rememberScrollState())
            ) {
                val currentCard = viewModel.currentCard.value
                if (currentCard != null) {
                    FlowRow(
                        content = {
                            currentCard.links.map {
                                LinkWithoutDeleteComponent(link = it)
                            }
                        })
                }

            }

            if (!viewModel.isFirstBox) {
                FilledTonalButton(
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                    onClick = viewModel::onCardGoesToPreviousBoxClicked
                ) {
                    Text(text = stringResource(R.string.move_card_to_previous_box_label))
                }
            }
            if (!viewModel.isLastBox) {
                FilledTonalButton(
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                    onClick = viewModel::onCardGoesToNextBoxClicked
                ) {
                    Text(text = stringResource(R.string.move_card_to_next_box_label))
                }
                FilledTonalButton(
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                    onClick = viewModel::onCardStaysInBoxClicked
                ) {
                    Text(text = stringResource(R.string.dont_move_card_text))
                }
            } else {
                FilledTonalButton(
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                    onClick = viewModel::onCardStaysInBoxClicked
                ) {
                    Text(text = stringResource(R.string.next_card_text))
                }
            }
        }
    }
}






