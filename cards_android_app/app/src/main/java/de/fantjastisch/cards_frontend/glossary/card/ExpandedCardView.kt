package de.fantjastisch.cards_frontend.glossary.card

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import de.fantjastisch.cards.R
import de.fantjastisch.cards_frontend.card.CardContextMenu
import de.fantjastisch.cards_frontend.glossary.GlossaryViewModel
import de.fantjastisch.cards_frontend.glossary.LinkWithoutDeleteComponent
import org.openapitools.client.models.CardEntity


/**
 * Zeigt eine ausgeklappte Karte im Glossar an.
 * @param card: Die Angezeigte Karte
 *
 * @author Tamari Bayer, Freja Sender, Jessica Repty, Semjon Nirmann
 * **/
@Composable
fun ExpandedCardView(
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