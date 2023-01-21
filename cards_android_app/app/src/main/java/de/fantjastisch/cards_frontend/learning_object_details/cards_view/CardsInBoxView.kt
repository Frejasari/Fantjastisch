package de.fantjastisch.cards_frontend.learning_object_details.cards_view

import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import de.fantjastisch.cards_frontend.card.CommonCardComponent


@Composable
fun CardsInBoxView(
    modifier: Modifier = Modifier,
    viewModel: CardsInBoxViewModel
) {
    LaunchedEffect(
        // wenn sich diese Variable ändert
        key1 = Unit,
        // dann wird dieses Lambda ausgeführt.
        block = {
            viewModel.onPageLoaded()
        })

    LazyColumn(
        modifier = modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .padding(5.dp)
    ) {
        items(viewModel.cardsInBox.value) { card ->
            CommonCardComponent(card = card, onCardSelected = {}, hasCheckBox = false)
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


