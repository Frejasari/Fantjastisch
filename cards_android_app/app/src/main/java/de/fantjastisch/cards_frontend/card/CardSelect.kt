package de.fantjastisch.cards_frontend.card

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.openapitools.client.models.CardEntity
import java.util.*

data class CardSelectItem(
    val card: CardEntity,
    val isChecked: Boolean
)

@Composable
fun CardSelect(
    modifier: Modifier = Modifier,
    cards: List<CardSelectItem>,
    onCardSelected: (UUID) -> Unit = {},

) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .padding(5.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        cards.map { card ->
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column (Modifier.weight(6.25f)) { CommonCardComponent(card = card.card) }
                Column (Modifier.weight(0.75f)) {
                    Checkbox(
                        checked = card.isChecked,
                        onCheckedChange = { onCardSelected(card.card.id) }
                    )
                }
            }

        }
    }
}