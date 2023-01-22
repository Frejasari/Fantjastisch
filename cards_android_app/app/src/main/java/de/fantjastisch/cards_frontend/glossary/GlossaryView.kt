package de.fantjastisch.cards_frontend.glossary

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import de.fantjastisch.cards_frontend.card.CardContextMenu
import de.fantjastisch.cards_frontend.card.delete.DeleteCardDialog
import de.fantjastisch.cards_frontend.glossary.GlossaryViewModel.DeletionProgress
import org.openapitools.client.models.CardEntity


@Composable
@Preview
fun GlossaryView(
    modifier: Modifier = Modifier
) {

    val viewModel = viewModel { GlossaryViewModel() }

    val deletionProgress = viewModel.currentDeleteDialog.value
    if (deletionProgress != null) {
        DeleteCardDialog(
            card = deletionProgress.card,
            isDeleteButtonEnabled = deletionProgress is DeletionProgress.ConfirmWithUser,
            onDismissClicked = { viewModel.onDeleteCardAborted() },
            onDeleteClicked = {
                viewModel.onDeleteCardClicked()
            }
        )
    }


    // Lädt die Cards neu, wenn wir aus einem anderen Tab wieder hier rein kommen.
    LaunchedEffect(
        // wenn sich diese Variable ändert
        key1 = Unit,
        // dann wird dieses Lambda ausgeführt.
        block = {
            viewModel.onPageLoaded()
        })

    // Ein RecyclerView -> Eine lange liste von Eintraegen
    LazyColumn(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp),
        contentPadding = PaddingValues(vertical = 16.dp)
    ) {
        items(viewModel.cards.value) { card ->
            CardView(card, viewModel)
        }
    }
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
private fun CardView(
    card: CardEntity,
    viewModel: GlossaryViewModel
) {
    Surface(
        modifier = Modifier,
        shadowElevation = 6.dp,
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp, horizontal = 16.dp),

            ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    modifier = Modifier
                        .weight(weight = 1f, fill = false),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    text = card.question
                )
                CardContextMenu(
                    cardId = card.id,
                    onDeleteClicked = { viewModel.onTryDeleteCard(card) },
                )
            }
            Divider(
                modifier = Modifier
                    .padding(vertical = 6.dp)
            )
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    modifier = Modifier,
                    text = card.tag
                )
                card.categories.forEach {
                    SuggestionChip(
                        modifier = Modifier,
                        onClick = { },
                        label = {
                            Text(
                                modifier = Modifier,
                                text = it.label
                            )
                        })
                }
            }
        }
    }
}

