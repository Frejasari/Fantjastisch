package de.fantjastisch.cards_frontend.learning_system

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import de.fantjastisch.cards.R
import java.util.*

//TODO Fehler anzeigen.
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateLearningSystemView(
        modifier: Modifier = Modifier
) {
    val viewModel = viewModel { CreateLearningSystemViewModel() }

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
                        onDone = { viewModel.onAddLearningSystemClicked() },
                ),
                modifier = Modifier.fillMaxWidth(),
                value = viewModel.learningSystemLabel.value,
                onValueChange = { viewModel.learningSystemLabel.value = it },
                placeholder = { Text(text = stringResource(id = R.string.create_category_label_text)) },
                label = { Text(text = "Bezeichnung") }
        )
        Divider()
        OutlinedTextField(
                maxLines = 1,
                keyboardActions = KeyboardActions(
                        onDone = { viewModel.onAddLearningSystemClicked() },
                ),
                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth(),
                value = if (viewModel.numBoxes.value == 0) "" else viewModel.numBoxes.value.toString(),
                onValueChange = { viewModel.onBoxesSelected(it) },
                placeholder = { Text(text = stringResource(id = R.string.select_num_of_boxes_label)) },
                label = { Text(text = "Anzahl der Lernkästen") }
        )
//        repeat (viewModel.numBoxes.value) { index ->
//            OutlinedTextField(
//                    maxLines = 1,
//                    modifier = Modifier.fillMaxWidth(),
//                    value = viewModel.learningSystemBoxLabels.value[index],
//                    onValueChange = { viewModel.learningSystemBoxLabels.value[index] = it },
//                    placeholder = { Text(text = stringResource(id = R.string.create_category_label_text)) },
//                    label = { Text(text = "Box " + (index + 1).toString()) }
//            )
//        }

        viewModel.learningSystemBoxLabels.value.forEachIndexed { index, element ->
            OutlinedTextField(
                    maxLines = 1,
                    modifier = Modifier.fillMaxWidth(),
                    value = viewModel.learningSystemBoxLabels.value[index],
                    onValueChange = { viewModel.onBoxLabelChanged(index, it) },
                    placeholder = { Text(text = stringResource(id = R.string.create_category_label_text)) },
                    label = { Text(text = "Box " + (index + 1).toString()) }
            )
        }
        Text(
                text = "Karten aus Kategorien hinzufügen",
                style = MaterialTheme.typography.titleMedium
        )
        FilledTonalButton(
                modifier = Modifier.align(Alignment.CenterHorizontally),
                onClick = viewModel::onAddLearningSystemClicked
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

