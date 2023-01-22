package de.fantjastisch.cards_frontend.link.delete

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import de.fantjastisch.cards.R
import java.util.*


@Composable
fun DeleteLinkDialog(
    name: String,
    linkId: UUID,
    isOpen: Boolean,
    setIsOpen: (isOpen: Boolean) -> Unit,
    onDeleteSuccessful: () -> Unit
) {
    val viewModel = viewModel(key = linkId.toString())
    { DeleteLinkViewModel(linkId = linkId) }
    if (isOpen) {
        AlertDialog(
            onDismissRequest = { setIsOpen(false) }, title = {
                Text(text = stringResource(R.string.delete_link_dialog_title))
            },
            text = {
                Text(name)
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