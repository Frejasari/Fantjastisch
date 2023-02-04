package de.fantjastisch.cards_frontend.category.delete

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import de.fantjastisch.cards.R
import org.openapitools.client.models.CategoryEntity

/**
 * Zeigt Nachfrage Delete Category in einem Dialog an.
 *
 * @param cat Zu löschende Kategorie
 * @param isDeleteButtonEnabled Enabled den DeleteButton, nach einmaligem Auslösen
 * @param onDismissClicked Callback, welches ausgelöst, wenn man den Dialog schließen möchte
 * @param onDeleteClicked Funktion, mit der die Karte gelöscht wird
 *
 * @author Tamari Bayer, Semjon Nirmann
 */
@Composable
fun DeleteCategoryDialog(
    cat: CategoryEntity,
    isDeleteButtonEnabled: Boolean = true,
    onDismissClicked: () -> Unit,
    onDeleteClicked: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismissClicked,
        title = {
            Text(text = stringResource(R.string.delete_category_dialog_title))
        },
        text = {
            Text(text = cat.label)
        },
        confirmButton = {
            Button(
                enabled = isDeleteButtonEnabled,
                onClick = onDeleteClicked
            ) {
                Text(text = stringResource(R.string.confirm))
            }
        },
        dismissButton = {
            Button(
                onClick = onDismissClicked
            ) {
                Text(text = stringResource(R.string.cancel))
            }
        }
    )
}
