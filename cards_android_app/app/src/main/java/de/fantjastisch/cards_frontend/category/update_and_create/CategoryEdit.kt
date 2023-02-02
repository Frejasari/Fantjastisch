package de.fantjastisch.cards_frontend.category.update_and_create

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import de.fantjastisch.cards.R
import de.fantjastisch.cards_frontend.card.update.TextFieldState
import de.fantjastisch.cards_frontend.category.CategorySelect
import de.fantjastisch.cards_frontend.category.CategorySelectItem
import de.fantjastisch.cards_frontend.components.OutlinedTextFieldWithErrors
import java.util.*


<<<<<<< HEAD
/**
 * Zeigt das Fenster um eine Kategorie zu bearbeiten.
 *
 * @param modifier Modifier für die Seite.
 * @param label Aktuelles Label der Kategorie.
 * @param categories Aktuelle Unterkategorien der Kategorie.
 * @param onCategorySelected Callback, wenn eine Kategorie ausgewählt wurde.
 * @param onUpdateCategoryClicked Funktion, mit der man die Kategorie bearbeitet.
 *
 * @author Tamari Bayer, Freja Sender
 */
=======
>>>>>>> f889dfb (refactor create/edit card view)
@SuppressLint("UnrememberedMutableState")
@Composable
fun CategoryEdit(
    modifier: Modifier = Modifier,
    label: TextFieldState,
    categories: List<CategorySelectItem>,
    onCategorySelected: (UUID) -> Unit,
    onUpdateCategoryClicked: () -> Unit,
) {
    Column(
        modifier = modifier
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp)
            .fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Column(modifier = Modifier.weight(1f)) {
            OutlinedTextFieldWithErrors(
                maxLines = 3,
                value = label.value,
                errors = label.errors,
                onValueChange = label.onValueChange,
                placeholder = stringResource(id = R.string.label_label),
                field = "label"
            )
            CategorySelect(
                categories = categories,
                onCategorySelected = onCategorySelected
            )
        }
        FilledTonalButton(
            modifier = Modifier.align(Alignment.CenterHorizontally),
            onClick = onUpdateCategoryClicked
        ) {
            Text(text = stringResource(R.string.save_button_text))
        }
    }
}





