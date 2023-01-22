package de.fantjastisch.cards_frontend.learning_overview.delete

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.lifecycle.viewmodel.compose.viewModel
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
    if (isOpen) {
        AlertDialog(
            onDismissRequest = { setIsOpen(false) }, title = {
                Text(text = "Möchtest du dieses Lernobjekt wirklich löschen?")
            },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.onDeleteClicked()
                    }) {
                    Text(text = "Ja")
                }
            },
            dismissButton = {
                Button(
                    onClick = {
                        setIsOpen(false)
                    }) {
                    Text(text = "Abbrechen")
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