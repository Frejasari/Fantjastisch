package de.fantjastisch.cards_frontend.link.delete

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import de.fantjastisch.cards.R
import org.openapitools.client.models.CardEntity
import org.openapitools.client.models.LinkEntity


@Composable
fun DeleteLinkDialog(
    link: LinkEntity,
    isDeleteButtonEnabled: Boolean = true,
    onDismissClicked: () -> Unit,
    onDeleteClicked: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismissClicked,
        title = {
            Text(text = stringResource(R.string.delete_link_dialog_title))
        },
        text = {
            Text(link.label!!)
        },
        confirmButton = {
            Button(
                enabled = isDeleteButtonEnabled,
                onClick = onDeleteClicked
            ) {
                Text(text = stringResource(id = R.string.confirm))
            }
        },
        dismissButton = {
            Button(
                onClick = onDismissClicked
            ) {
                Text(text = stringResource(id = R.string.cancel))
            }
        }
    )
}