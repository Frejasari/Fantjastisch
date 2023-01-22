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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import de.fantjastisch.cards.R
import de.fantjastisch.cards_frontend.components.OutlinedTextFieldWithErrors
import de.fantjastisch.cards_frontend.infrastructure.CloseScreenOnSignalEffect
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
        OutlinedTextFieldWithErrors(
            value = viewModel.learningSystemLabel.value,
            onValueChange = { viewModel.learningSystemLabel.value = it },
            placeholder = stringResource(id = R.string.create_category_label_text),
            errors = viewModel.errors.value,
            field = "label",
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
            label = { Text(text = "Anzahl der Lernkästen") },
            isError = viewModel.numBoxes.value == 0
        )

        viewModel.learningSystemBoxLabels.value.forEachIndexed { index, element ->
            OutlinedTextFieldWithErrors(
                value = viewModel.learningSystemBoxLabels.value[index],
                onValueChange = { viewModel.onBoxLabelChanged(index, it) },
                placeholder = stringResource(id = R.string.create_category_label_text),
                errors = viewModel.errors.value,
                field = "boxLabels",
            )
        }
        FilledTonalButton(
            modifier = Modifier.align(Alignment.CenterHorizontally),
            onClick = viewModel::onAddLearningSystemClicked
        ) {
            Text(text = stringResource(R.string.create_category_save_button_text))
        }
    }
    CloseScreenOnSignalEffect(shouldClose = viewModel.isFinished.value)

}

