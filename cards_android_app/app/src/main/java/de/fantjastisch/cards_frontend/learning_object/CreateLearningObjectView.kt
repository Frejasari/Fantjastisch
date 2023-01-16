package de.fantjastisch.cards_frontend.learning_object

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material3.*
import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import de.fantjastisch.cards.R
import de.fantjastisch.cards_frontend.category.CategorySelect
import de.fantjastisch.cards_frontend.learning_system.LearningSystemSelect
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
            label = {Text(text="Bezeichnung")}
        )
        Divider()
        Text(
                text = "Lernsystem wählen",
                style = MaterialTheme.typography.titleMedium
        )
        LearningSystemSelect(
                modifier = Modifier.weight(1f),
                learningSystems = viewModel.learningSystems.value,
                onLearningSystemSelected = viewModel::onLearningSystemSelected,
        )
        Divider()
        Text(
                text = "Karten aus Kategorien hinzufügen",
                style = MaterialTheme.typography.titleMedium
        )
        CategorySelect(
            modifier = Modifier.weight(1f),
            categories = viewModel.categories.value,
            onCategorySelected = viewModel::onCategorySelected
        )
        Divider()
        Text(
                text = "Einzelne Karten hinzufügen",
                style = MaterialTheme.typography.titleMedium
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

