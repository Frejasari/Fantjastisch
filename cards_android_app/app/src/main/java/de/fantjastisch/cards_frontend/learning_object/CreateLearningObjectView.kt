package de.fantjastisch.cards_frontend.learning_object

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.verticalScroll
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
import de.fantjastisch.cards_frontend.card.CardSelect
import de.fantjastisch.cards_frontend.category.CategorySelect
import de.fantjastisch.cards_frontend.components.SingleSelect
import java.util.*

//TODO Fehler anzeigen.
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateLearningObjectView(
    modifier: Modifier = Modifier
) {
    val viewModel = viewModel { CreateLearningObjectViewModel() }

    // Componente die ihre Kinder untereinander anzeigt.
    Column(
        modifier = modifier
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
            .fillMaxSize()
    ) {
        OutlinedTextField(
            maxLines = 1,
            keyboardActions = KeyboardActions(
                onDone = { viewModel.onAddLearningObjectClicked() },
            ),
            modifier = Modifier.fillMaxWidth(),
            value = viewModel.learningObjectLabel.value,
            onValueChange = { viewModel.learningObjectLabel.value = it },
            placeholder = { Text(text = stringResource(id = R.string.create_category_label_text)) },
            label = { Text(text = "Bezeichnung") },
            isError = viewModel.learningObjectLabel.value.isBlank()
        )
        Divider(Modifier.padding(horizontal = 20.dp, vertical = 20.dp))
        SingleSelect(
            items = viewModel.learningSystems.value,
            selectedItem = viewModel.selectedSystem.value,
            onItemSelected = viewModel::onLearningSystemSelected,
            placeholder = { Text(text = "Lernsystem") }
        )
        Divider(Modifier.padding(horizontal = 20.dp, vertical = 20.dp))
        Text(
            text = "Kategorien hinzufügen",
            style = MaterialTheme.typography.titleMedium
        )
        CategorySelect(
            categories = viewModel.categories.value,
            onCategorySelected = viewModel::onCategorySelected
        )
        Divider(Modifier.padding(horizontal = 20.dp, vertical = 20.dp))
        Text(
            text = "Einzelne Karten hinzufügen",
            style = MaterialTheme.typography.titleMedium
        )
        CardSelect(
            cards = viewModel.cards.value,
            onCardSelected = viewModel::onCardSelected
        )

        FilledTonalButton(
            modifier = Modifier.align(Alignment.CenterHorizontally),
            onClick = viewModel::onAddLearningObjectClicked
        ) {
            Text(text = stringResource(R.string.create_category_save_button_text))
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

