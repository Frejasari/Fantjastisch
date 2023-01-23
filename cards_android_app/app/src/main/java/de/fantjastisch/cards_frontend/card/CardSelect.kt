package de.fantjastisch.cards_frontend.card

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.material3.Checkbox
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import org.openapitools.client.models.CardEntity
import java.util.*

data class CardSelectItem(
    val card: CardEntity,
    val isChecked: Boolean
)


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
                Column(Modifier.weight(6.25f)) { CommonCardComponent(card = card.card) }
                Column(Modifier.weight(0.75f)) {
                    Checkbox(
                        checked = card.isChecked,
                        onCheckedChange = { onCardSelected(card.card.id) }
                    )
                }
            }
        }
    }
}