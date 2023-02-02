package de.fantjastisch.cards_frontend.glossary

import android.annotation.SuppressLint
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import de.fantjastisch.cards.R
import de.fantjastisch.cards_frontend.card.CardContextMenu
import de.fantjastisch.cards_frontend.card.delete.DeleteCardDialog
import de.fantjastisch.cards_frontend.glossary.GlossaryViewModel.DeletionProgress
import de.fantjastisch.cards_frontend.util.formatToInlineLabel
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
                ContractedCard(card)

            } else {
                ExpandedCard(card)
            }
        }
    }
}

/**
 * Zeigt eine ausgeklappte Karte im Glossar an.
 *
 * @author Tamari Bayer, Freja Sender, Jessica Repty, Semjon Nirmann
 * **/
@Composable
private fun ExpandedCard(
    card: CardEntity
) {
    val viewModel = viewModel { GlossaryViewModel() }
    Row(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = Modifier
                .weight(weight = 1f)
        ) {
            Text(
                text = card.question
            )
        }
        CardContextMenu(
            cardId = card.id,
            onDeleteClicked = { viewModel.onTryDeleteCard(card) },
        )
    }
    Divider(
        modifier = Modifier
            .padding(vertical = 10.dp)
    )

    TwoTextsWithDivider(
        headline = stringResource(R.string.answer_label),
        text = card.answer
    )

    TwoTextsWithDivider(
        headline = stringResource(R.string.answer_label),
        text = card.tag
    )

    TwoTextsWithDivider(
        headline = stringResource(R.string.categories_label),
        text = card.categories
            .joinToString(separator = ", ") { category -> category.label },
    )

    Text(
        text = stringResource(R.string.links_label),
        fontWeight = FontWeight.Light,
    )
    LazyRow {
        item {
            card.links.forEach {
                LinkWithoutDeleteComponent(link = it)
            }
        }
    }
}


/**
 * Zeigt 2 Texte übereinander an und darunter einen Divider.
 *
 * @author Freja Sender
 * **/
@Composable
private fun TwoTextsWithDivider(headline: String, text: String) {
    Text(
        text = headline,
        fontWeight = FontWeight.Light
    )
    Text(
        text = text
    )

    Divider(
        modifier = Modifier
            .padding(vertical = 10.dp)
    )
}

/**
 * Zeigt eine eingeklappte Karte im Glossar an.
 *
 * @author Tamari Bayer, Freja Sender, Jessica Repty, Semjon Nirmann
 * **/
@Composable
private fun ContractedCard(
    card: CardEntity
) {
    val viewModel = viewModel { GlossaryViewModel() }
    Row(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = Modifier
                .weight(weight = 1f),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                text = card.question,
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(R.string.tag_label).formatToInlineLabel(),
                    fontWeight = FontWeight.Medium,
                )
                Text(
                    modifier = Modifier,
                    text = card.tag,
                )
            }
        }
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
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = stringResource(R.string.categories_label).formatToInlineLabel(),
            fontWeight = FontWeight.Medium,
        )
        Text(
            text = card.categories
                .joinToString(separator = ", ") { category -> category.label },
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}



