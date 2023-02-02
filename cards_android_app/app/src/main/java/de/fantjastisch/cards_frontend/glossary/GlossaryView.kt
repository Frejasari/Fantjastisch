package de.fantjastisch.cards_frontend.glossary

import android.annotation.SuppressLint
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import de.fantjastisch.cards_frontend.card.delete.DeleteCardDialog
import de.fantjastisch.cards_frontend.glossary.GlossaryViewModel.DeletionProgress
import de.fantjastisch.cards_frontend.glossary.card.CollapsedCardView
import de.fantjastisch.cards_frontend.glossary.card.ExpandedCardView
import kotlinx.coroutines.launch
import org.openapitools.client.models.CardEntity
import java.util.*

/**
 * Rendert die Glossar Seite
 *
 * @author Tamari Bayer, Freja Sender, Jessica Repty, Semjon Nirmann
 * **/
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

    val listState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()


    // Ein RecyclerView -> Eine lange liste von Eintraegen
    LazyColumn(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp),
        contentPadding = PaddingValues(vertical = 16.dp),
        state = listState
    ) {
        itemsIndexed(viewModel.cards.value) { index, card ->
            GlossaryCardView(card) {
                coroutineScope.launch {
                    // Animate scroll to the 10th item
                    listState.animateScrollToItem(index = index)
                }
            }
        }
    }
}

/**
 * Rendert die Glossar Seite
 *
 * @author Tamari Bayer, Freja Sender, Jessica Repty, Semjon Nirmann
 * **/
@SuppressLint("UnrememberedMutableState")
@Composable
private fun GlossaryCardView(
    card: CardEntity,
    onItemExpanded: () -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Surface(
        modifier = Modifier.clickable(
            onClick = {
                expanded = !expanded
                onItemExpanded()
            }
        ),
        shadowElevation = 6.dp,
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp, horizontal = 16.dp),

            ) {
            if (!expanded) {
                CollapsedCardView(card)

            } else {
                ExpandedCardView(card)
            }
        }
    }
}

