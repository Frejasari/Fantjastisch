package de.fantjastisch.cards_frontend.category.update_and_create

import androidx.compose.material3.Divider
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import de.fantjastisch.cards.R
import de.fantjastisch.cards_frontend.card.update.TextFieldState
import de.fantjastisch.cards_frontend.category.CategorySelect
import de.fantjastisch.cards_frontend.category.CategorySelectItem
import de.fantjastisch.cards_frontend.components.ExpandableRow
import de.fantjastisch.cards_frontend.components.OutlinedTextFieldWithErrors
import de.fantjastisch.cards_frontend.components.SaveLayout
import java.util.*

/**
 * Zeigt das Fenster um eine Kategorie zu bearbeiten.
 *
 * @param modifier Modifier für die Seite.
 * @param label Aktuelles Label der Kategorie.
 * @param categories Aktuelle Unterkategorien der Kategorie.
 * @param onCategorySelected Callback, wenn eine Kategorie ausgewählt wurde.
 * @param onUpdateCategoryClicked Funktion, mit der man die Kategorie bearbeitet.
 *
 * @author Tamari Bayer, Freja Sender, Semjon Nirmann
 */
@Composable
fun CategoryEdit(
    modifier: Modifier = Modifier,
    label: TextFieldState,
    categories: List<CategorySelectItem>,
    onCategorySelected: (UUID) -> Unit,
    onUpdateCategoryClicked: () -> Unit,
) {
    var expanded by remember { mutableStateOf(true) }
    var expandedForCat by remember { mutableStateOf(false) }
    SaveLayout(
        onSaveClicked = onUpdateCategoryClicked,
        modifier = modifier
    ) {

        ExpandableRow(
            expanded = expanded,
            onClick = {
                expandedForCat = false
                expanded = !expanded
            },
            headline = stringResource(R.string.allgemein_category_label),
        ) {
            OutlinedTextFieldWithErrors(
                maxLines = 3,
                value = label.value,
                errors = label.errors,
                onValueChange = label.onValueChange,
                placeholder = stringResource(id = R.string.label_label),
                field = "label"
            )
        }

        Divider()

        ExpandableRow(
            expanded = expandedForCat,
            onClick = {
                expandedForCat = !expandedForCat
                expanded = false
            },
            headline = stringResource(id = R.string.sub_categories_label),
        ) {
            CategorySelect(
                categories = categories,
                onCategorySelected = onCategorySelected
            )
        }
    }
}





