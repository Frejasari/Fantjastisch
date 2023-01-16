package de.fantjastisch.cards_frontend.card

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import java.util.*

data class CardSelectItem(
    val id: UUID, val question: String, val answer: String, val tag: String,
    val categories: List<UUID>, val isChecked: Boolean
)

@Composable
fun CardSelect(
    modifier: Modifier = Modifier,
    cards: List<CardSelectItem>,
    onCardSelected: (UUID) -> Unit = {}
) {
    // Ein RecyclerView -> Eine lange liste von Eintraegen
    LazyColumn(
        modifier = modifier
            .fillMaxWidth(),
        contentPadding = PaddingValues(vertical = 16.dp)
    ) {
        items(cards) { card ->
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    modifier = Modifier
                        .weight(1f),
                    text = card.question
                )
                Text(
                    modifier = Modifier
                        .weight(1f),
                    text = card.answer
                )
                Text(
                    modifier = Modifier
                        .weight(1f),
                    text = card.tag
                )
                Text(
                    modifier = Modifier
                        .weight(1f),
                    text = card.categories.toString()
                )
                Checkbox(
                    checked = card.isChecked,
                    onCheckedChange = { onCardSelected(card.id) }
                )
            }
        }
    }
}