package de.fantjastisch.cards_frontend.card.delete

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import de.fantjastisch.cards.R
import org.openapitools.client.models.CardEntity


@Composable
fun DeleteCardDialog(
    card: CardEntity,
    isDeleteButtonEnabled: Boolean = true,
    onDismissClicked: () -> Unit,
    onDeleteClicked: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismissClicked,
        title = {
            Text(text = stringResource(R.string.delete_card_dialog_title))
        },
        text = {
            Text(card.question)
        },
        confirmButton = {
            Button(
                onClick = onDeleteClicked
            ) {
                Text(text = "Ja")
            }
        },
        dismissButton = {
            Button(
                enabled = isDeleteButtonEnabled,
                onClick = onDismissClicked
            ) {
                Text(text = "Abbrechen")
            }
        }
    )
}