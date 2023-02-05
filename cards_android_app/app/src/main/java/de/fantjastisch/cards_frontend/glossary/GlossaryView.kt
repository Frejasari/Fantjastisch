package de.fantjastisch.cards_frontend.glossary

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.YoutubeSearchedFor
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import de.fantjastisch.cards.R
import de.fantjastisch.cards_frontend.card.delete.DeleteCardDialog
import de.fantjastisch.cards_frontend.glossary.GlossaryViewModel.DeletionProgress
import de.fantjastisch.cards_frontend.glossary.card.GlossaryCardView
import de.fantjastisch.cards_frontend.infrastructure.effects.OnFirstLoadedSignalEffect
import de.fantjastisch.cards_frontend.infrastructure.effects.ShowErrorOnSignalEffect
import kotlinx.coroutines.launch

/**
 * Rendert die Glossar Seite
 *
 * @param modifier Modifier für die Seite.
 *
 * @author Tamari Bayer, Freja Sender, Jessica Repty, Semjon Nirmann
 */
@Composable
@Preview
fun GlossaryView(
    modifier: Modifier = Modifier
) {

    val viewModel = viewModel { GlossaryViewModel() }
    ShowErrorOnSignalEffect(viewModel = viewModel)
    OnFirstLoadedSignalEffect(onPageLoaded = viewModel::onPageLoaded)
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

    val noCards = viewModel.cards.value.isEmpty()
    if (noCards) {
        Box(
            modifier = modifier
                .padding(16.dp)
                .fillMaxSize(),
            contentAlignment = Alignment.TopCenter
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(10.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    modifier = Modifier
                        .alpha(0.5f)
                        .fillMaxWidth(0.5f)
                        .aspectRatio(1f),
                    imageVector = Icons.Default.YoutubeSearchedFor,
                    contentDescription = "",
                )
                Text(
                    textAlign = TextAlign.Center,
                    text = stringResource(R.string.no_cards_found)
                )
            }
        }
    } else {

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
                        listState.animateScrollToItem(index = index)
                    }
                }
            }
        }
    }
}
