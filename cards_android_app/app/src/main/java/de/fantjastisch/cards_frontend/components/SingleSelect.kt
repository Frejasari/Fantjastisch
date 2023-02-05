package de.fantjastisch.cards_frontend.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.toSize
import org.openapitools.client.models.ErrorEntryEntity
import java.util.*

/**
 * Hält die auswählbaren Elemente, für SingleSelect.
 *
 * @property label Label, des Elements.
 * @property id Id, des Elements.
 * @author Semjon Nirmann, Jessica Repty, Freja Sender
 */
data class SingleSelectItem(val label: String, val id: UUID)

/**
 * View für ein Drop-Down-Menü und Auswählen eines Elementes.
 *
 * @param items Alle auswählbaren Elemente.
 * @param onItemSelected Callback, wenn Click auf ein Element.
 * @param selectedItem Ausgewähltes Element.
 * @param placeholder Label, welches angezeigt wird, wenn noch kein Element ausgewählt.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SingleSelect(
    items: List<SingleSelectItem>,
    onItemSelected: (UUID) -> Unit = {},
    selectedItem: SingleSelectItem?,
    placeholder: String,
    errors: List<ErrorEntryEntity>,
    field: String
) {
    val isExpanded = remember { mutableStateOf(false) }
    val isSelected = remember { mutableStateOf(false) }
    val textFieldSize = remember { mutableStateOf(Size.Zero) }

    val icon = if (isExpanded.value)
        Icons.Filled.KeyboardArrowUp
    else
        Icons.Filled.KeyboardArrowDown
    val error = errors.find { it.field == field }

    Column() {
        OutlinedTextFieldWithErrors(
            value = selectedItem?.label ?: "",
            onValueChange = { },
            modifier = Modifier
                .clickable { isExpanded.value = !isExpanded.value }
                .onGloballyPositioned { coordinates ->
                    textFieldSize.value = coordinates.size.toSize()
                },
            placeholder = placeholder,
            trailingIcon = {
                Icon(icon, "contentDescription")
            },
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = Color(0xFF6650a4),
                unfocusedBorderColor = Color(0xFF625b71),
                cursorColor = Color.Transparent,
                errorCursorColor = Color.Transparent,
                textColor = if (error != null) MaterialTheme.colorScheme.error else Color(
                    0xFF625b71
                ),
                disabledTextColor = if (error != null) MaterialTheme.colorScheme.error else Color(
                    0xFF625b71
                ),
                disabledBorderColor = if (error != null) MaterialTheme.colorScheme.error else Color(
                    0xFF625b71
                ),
                disabledLabelColor = if (error != null) MaterialTheme.colorScheme.error else Color(
                    0xFF625b71
                ),
                disabledPlaceholderColor = if (error != null) MaterialTheme.colorScheme.error else Color(
                    0xFF625b71
                )
            ),
            errors = errors,
            enabled = false,
            field = field
        )

        DropdownMenu(
            expanded = isExpanded.value,
            onDismissRequest = { isExpanded.value = false },
            modifier = Modifier
                .width(with(LocalDensity.current) { textFieldSize.value.width.toDp() })
        ) {
            items.forEach { item ->
                DropdownMenuItem(
                    onClick = {
                        onItemSelected(item.id)
                        isSelected.value = true
                        isExpanded.value = false
                    },
                    text = { Text(text = item.label, color = Color.Black) }
                )
            }
        }
    }
}