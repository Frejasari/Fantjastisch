package de.fantjastisch.cards_frontend.learning_system

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.lifecycle.viewmodel.compose.viewModel
import de.fantjastisch.cards.R
import de.fantjastisch.cards_frontend.components.OutlinedTextFieldWithErrors
import de.fantjastisch.cards_frontend.components.SaveLayout
import de.fantjastisch.cards_frontend.infrastructure.effects.CloseScreenOnSignalEffect
import de.fantjastisch.cards_frontend.infrastructure.effects.ShowErrorOnSignalEffect
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateLearningSystemView(
    modifier: Modifier = Modifier
) {
    val viewModel = viewModel { CreateLearningSystemViewModel() }
    CloseScreenOnSignalEffect(shouldClose = viewModel.isFinished.value)
    ShowErrorOnSignalEffect(viewModel = viewModel)

    // Componente die ihre Kinder untereinander anzeigt.
    SaveLayout(
        onSaveClicked = viewModel::onAddLearningSystemClicked,
        modifier = modifier
    ) {
        OutlinedTextFieldWithErrors(
            value = viewModel.learningSystemLabel.value,
            onValueChange = { viewModel.learningSystemLabel.value = it },
            placeholder = stringResource(id = R.string.label_label),
            errors = viewModel.errors.value,
            field = "label",
        )
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
            label = { Text(text = stringResource(R.string.number_of_learningboxes_text)) },
            isError = viewModel.numBoxes.value == 0
        )

        viewModel.learningSystemBoxLabels.value.forEachIndexed { index, _ ->
            OutlinedTextFieldWithErrors(
                value = viewModel.learningSystemBoxLabels.value[index],
                onValueChange = { viewModel.onBoxLabelChanged(index, it) },
                placeholder = stringResource(id = R.string.label_label),
                errors = viewModel.errors.value,
                field = "boxLabels",
            )
        }
    }

}
