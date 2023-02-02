package de.fantjastisch.cards_frontend.glossary.card

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.*
import de.fantjastisch.cards_frontend.components.ExpandableCard
import org.openapitools.client.models.CardEntity


/**
 * Rendert die Glossar Seite
 *
 * @author Tamari Bayer, Freja Sender, Jessica Repty, Semjon Nirmann
 * **/
@Composable
fun GlossaryCardView(
    card: CardEntity,
    onItemExpanded: () -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    ExpandableCard(onClick = {
        expanded = !expanded
        onItemExpanded()
    }) {
        Column {
            if (!expanded) {
                CollapsedCardView(card)

            } else {
                ExpandedCardView(card)
            }
        }
    }
}
