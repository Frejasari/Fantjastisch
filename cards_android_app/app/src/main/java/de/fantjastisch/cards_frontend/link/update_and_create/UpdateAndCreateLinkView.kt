package de.fantjastisch.cards_frontend.card

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import de.fantjastisch.cards.R
import de.fantjastisch.cards_frontend.card.update_and_create.UpdateAndCreateCardViewModel
import de.fantjastisch.cards_frontend.category.CategorySelect
import de.fantjastisch.cards_frontend.components.OutlinedTextFieldWithErrors
import de.fantjastisch.cards_frontend.link.update_and_create.UpdateAndCreateLinkViewModel
import org.openapitools.client.models.ErrorEntryEntity


//TODO Fehler anzeigen.
@Composable
fun UpdateAndCreateLinkView(
    modifier: Modifier = Modifier,
    viewModel: UpdateAndCreateLinkViewModel
) {

    // Componente die ihre Kinder untereinander anzeigt.
    Column(
        modifier = modifier
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp)
            .fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        OutlinedTextFieldWithErrors(
            maxLines = 3,
            value = viewModel.linkName.value,
            errors = viewModel.errors.value,
            onValueChange = { viewModel.linkName.value = it },
            placeholder = stringResource(id = R.string.create_link_name),
            field = "name"
        )
        CardSelect(
            modifier = Modifier.weight(1f),
            cards = viewModel.allCards.value,
            onCardSelected = viewModel::onCardSelected
        )
        FilledTonalButton(
            modifier = Modifier.align(Alignment.CenterHorizontally),
            onClick = viewModel::save
        ) {
            Text(text = stringResource(R.string.create_card_save_button_text))
        }
    }

    val navigator = LocalNavigator.currentOrThrow
    // einmaliger Effekt
    LaunchedEffect(
        // wenn sich diese Variable ändert
        key1 = viewModel.isFinished.value,
        // dann wird dieses Lambda ausgeführt.
        block = {
            if (viewModel.isFinished.value) {
                navigator.pop()
            }
        })
}


