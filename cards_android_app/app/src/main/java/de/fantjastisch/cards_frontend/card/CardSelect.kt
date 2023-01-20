package de.fantjastisch.cards_frontend.card

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import java.util.*

data class CardSelectItem(
    val id: UUID, val question: String, val answer: String, val tag: String,
    val categories: List<String>, val isChecked: Boolean
)

@Composable
fun CardSelect(
    modifier: Modifier = Modifier,
    cards: List<CardSelectItem>,
    onCardSelected: (UUID) -> Unit = {}
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .padding(5.dp)
    ) {
        cards.map { card ->
            CardSelectComponent(card = card, onCardSelected = onCardSelected)
        }
    }
}