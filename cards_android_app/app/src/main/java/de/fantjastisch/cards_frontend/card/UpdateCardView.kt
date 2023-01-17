package de.fantjastisch.cards_frontend.card

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import de.fantjastisch.cards.R
import de.fantjastisch.cards_frontend.category.CategorySelect
import java.util.*

//TODO Fehler anzeigen.
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UpdateCardView(
    id: UUID,
    modifier: Modifier = Modifier,

) {
    val viewModel = viewModel { UpdateCardViewModel(id) }

    // Componente die ihre Kinder untereinander anzeigt.
    Column(
        modifier = modifier
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp)
            .fillMaxSize()
    ) {
        OutlinedTextField(
            maxLines = 1,
            keyboardActions = KeyboardActions(
                onDone = { viewModel.onUpdateCardClicked() },
            ),
            modifier = Modifier.fillMaxWidth(),
            value = viewModel.cardQuestion.value,
            supportingText = {
                val questionError = viewModel.errors.value.find { it.field == "question" }
                if (questionError != null) {
                    Text(text = mapError(questionError.code))
                }
            },
            onValueChange = { viewModel.cardQuestion.value = it },
            placeholder = { Text(text = viewModel.cardQuestion.value) },
        )
        OutlinedTextField(
            maxLines = 1,
            keyboardActions = KeyboardActions(
                onDone = { viewModel.onUpdateCardClicked() },
            ),
            modifier = Modifier.fillMaxWidth(),
            value = viewModel.cardAnswer.value,
            onValueChange = { viewModel.cardAnswer.value = it },
            placeholder = { Text(text = viewModel.cardAnswer.value) },
        )
        OutlinedTextField(
            maxLines = 1,
            keyboardActions = KeyboardActions(
                onDone = { viewModel.onUpdateCardClicked() },
            ),
            modifier = Modifier.fillMaxWidth(),
            value = viewModel.cardTag.value,
            onValueChange = { viewModel.cardTag.value = it },
            placeholder = { Text(text = viewModel.cardTag.value) },
        )
        CategorySelect(
            modifier = Modifier.weight(1f),
            categories = viewModel.cardCategories.value,
            onCategorySelected = viewModel::onCategorySelected
        )
        FilledTonalButton(
            modifier = Modifier.align(Alignment.CenterHorizontally),
            onClick = viewModel::onUpdateCardClicked
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





