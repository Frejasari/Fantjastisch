package de.fantjastisch.cards_frontend.glossary.card

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import de.fantjastisch.cards.R
import de.fantjastisch.cards_frontend.card.CardContextMenu
import de.fantjastisch.cards_frontend.glossary.GlossaryViewModel
import de.fantjastisch.cards_frontend.util.formatToInlineLabel
import org.openapitools.client.models.CardEntity


/**
 * Zeigt eine eingeklappte Karte im Glossar an.
 *
 * @author Tamari Bayer, Freja Sender, Jessica Repty, Semjon Nirmann
 * **/
@Composable
fun ContractedCard(
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



