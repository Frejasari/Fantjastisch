package de.fantjastisch.cards_frontend.learning_overview.delete

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import de.fantjastisch.cards.R
import de.fantjastisch.cards_frontend.infrastructure.effects.ShowErrorOnSignalEffect
import java.util.*


@Composable
fun DeleteLearningObjectDialog(
    learningObjectId: UUID,
    isOpen: Boolean,
    setIsOpen: (isOpen: Boolean) -> Unit,
    onDeleteSuccessful: () -> Unit
) {
    val viewModel = viewModel(key = learningObjectId.toString())
    { DeleteLearningObjectViewModel(learningObjectId = learningObjectId) }
    ShowErrorOnSignalEffect(viewModel = viewModel)

    if (isOpen) {
        AlertDialog(
            onDismissRequest = { setIsOpen(false) },
            title = {
                Text(text = stringResource(R.string.delete_learningobject_dialog_title))
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        viewModel.onDeleteClicked()
                    }) {
                    Text(text = stringResource(id = R.string.confirm))
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        setIsOpen(false)
                    }) {
                    Text(text = stringResource(id = R.string.cancel))
                }
            }
        )
    }
    // einmaliger Effekt
    LaunchedEffect(
        // wenn sich diese Variable ändert
        key1 = viewModel.isFinished.value,
        // dann wird dieses Lambda ausgeführt.
        block = {
            if (viewModel.isFinished.value) {
                onDeleteSuccessful()
                setIsOpen(false)
            }
        })
}