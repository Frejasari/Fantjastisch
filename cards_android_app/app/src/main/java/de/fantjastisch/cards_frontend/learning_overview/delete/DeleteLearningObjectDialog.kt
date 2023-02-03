package de.fantjastisch.cards_frontend.learning_overview.delete

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import de.fantjastisch.cards.R
import java.util.*

/**
 * Zeigt Nachfrage Delete LearningObject in einem Dialog an.
 *
 * @param learningObjectId Die UUID des zu löschenden Lernobjekts.
 * @param isOpen Gibt an, ob der Dialog geöffnet ist.
 * @param setIsOpen Die Funktion, die den Dialog anzeigen lässt.
 * @param onDeleteSuccessful Die Funktion, die ausgeführt wird,
 *   wenn ein Lernobjekt erfolgreich gelöscht wird.
 *
 * @author
 */
@Composable
fun DeleteLearningObjectDialog(
    learningObjectId: UUID,
    isOpen: Boolean,
    setIsOpen: (isOpen: Boolean) -> Unit,
    onDeleteSuccessful: () -> Unit
) {
    val viewModel = viewModel(key = learningObjectId.toString())
    { DeleteLearningObjectViewModel(learningObjectId = learningObjectId) }
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