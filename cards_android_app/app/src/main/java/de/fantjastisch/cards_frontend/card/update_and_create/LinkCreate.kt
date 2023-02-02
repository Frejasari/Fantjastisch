package de.fantjastisch.cards_frontend.card.update_and_create

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import de.fantjastisch.cards.R
import de.fantjastisch.cards_frontend.card.CardSelect
import de.fantjastisch.cards_frontend.card.CardSelectItem
import de.fantjastisch.cards_frontend.card.update.TextFieldState
import de.fantjastisch.cards_frontend.components.OutlinedTextFieldWithErrors
import java.util.*


@Composable
fun ColumnScope.LinkCreate(
    linkName: TextFieldState,
    cards: List<CardSelectItem>,
    onCardSelected: (UUID) -> Unit
) {
    OutlinedTextFieldWithErrors(
        maxLines = 1,
        value = linkName.value,
        errors = linkName.errors,
        onValueChange = linkName.onValueChange,
        placeholder = stringResource(id = R.string.create_link_name),
        field = "linkName"
    )
    LazyColumn(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.background)
            .weight(1f),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        CardSelect(
            cards = cards,
            onCardSelected = onCardSelected,
        )
    }
}

