package de.fantjastisch.cards_frontend.card

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.openapitools.client.models.CardEntity
import java.util.*

/**
 * Data Class, die die Select-Items für Karten beschreibt
 *
 *
 * @author Semjon Nirmann, Freja Sender
 */
data class CardSelectItem(
    val card: CardEntity,
    val isChecked: Boolean
)

/**
 * Rendert eine Liste an von Karten, die selektiert werden können.
 *
 * @param cards die Karten die angezeigt werden sollen
 * @param onCardSelected Callback, wenn eine Karte ausgewählt wurde.
 *
 * @author Semjon Nirmann, Freja Sender
 */
@Suppress("FunctionName")
fun LazyListScope.CardSelect(
    cards: List<CardSelectItem>,
    onCardSelected: (UUID) -> Unit = {},
) {

    cards.map { card ->
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(
                    modifier = Modifier
                        .weight(6.25f)
                ) {
                    Card(
                        shape = CardDefaults.elevatedShape,
                        elevation = CardDefaults.cardElevation(8.dp),
                        colors = CardDefaults.elevatedCardColors(
                            contentColor = MaterialTheme.colorScheme.onBackground,
                            containerColor = MaterialTheme.colorScheme.background
                        )
                    ) {
                        Column(
                            modifier = Modifier
                                .padding(vertical = 8.dp, horizontal = 16.dp),
                        ) {
                            CollapsedCardView(card = card.card)
                        }
                    }
                }
                Column(
                    modifier = Modifier.weight(0.75f)
                ) {
                    Checkbox(
                        checked = card.isChecked,
                        onCheckedChange = { onCardSelected(card.card.id) }
                    )
                }
            }
        }
    }
}