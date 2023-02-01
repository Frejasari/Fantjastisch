package de.fantjastisch.cards_frontend.category

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import de.fantjastisch.cards.R
import org.openapitools.client.models.CategoryEntity


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
            Text(cat.label)
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
