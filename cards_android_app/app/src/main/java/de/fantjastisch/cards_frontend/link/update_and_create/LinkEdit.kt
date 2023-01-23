package de.fantjastisch.cards_frontend.link

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import de.fantjastisch.cards.R
import de.fantjastisch.cards_frontend.card.CardSelect
import de.fantjastisch.cards_frontend.card.CardSelectItem
import de.fantjastisch.cards_frontend.card.update.TextFieldState
import de.fantjastisch.cards_frontend.category.CategorySelect
import de.fantjastisch.cards_frontend.category.CategorySelectItem
import de.fantjastisch.cards_frontend.components.OutlinedTextFieldWithErrors
import java.util.*


@Composable
fun LinkEdit(
    modifier: Modifier = Modifier,
    name: TextFieldState,
    cards: List<CardSelectItem>,
    onCardSelected: (UUID) -> Unit,
    onUpdateLinkClicked: () -> Unit,
) {
    Column(
        modifier = modifier
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp)
            .fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        OutlinedTextFieldWithErrors(
            maxLines = 3,
            value = name.value,
            errors = name.errors,
            onValueChange = name.onValueChange,
            placeholder = stringResource(id = R.string.create_link_name),
            field = "name"
        )
        Column(modifier = Modifier.fillMaxSize()) {
            // Componente die ihre Kinder untereinander anzeigt.
            LazyColumn(
                modifier = modifier
                    .background(MaterialTheme.colorScheme.background)
                    .weight(1f),
                contentPadding = PaddingValues(all = 16.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
               CardSelect(
                    cards = cards,
                    onCardSelected = onCardSelected,
                )
            }
            FilledTonalButton(
                modifier = Modifier.align(Alignment.CenterHorizontally),
                onClick = onUpdateLinkClicked
            ) {
                Text(text = stringResource(R.string.create_card_save_button_text))
            }
        }

    }
}

