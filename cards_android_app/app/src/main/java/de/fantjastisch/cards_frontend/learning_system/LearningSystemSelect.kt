package de.fantjastisch.cards_frontend.learning_system

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.toSize
import java.util.*

data class SingleSelectItem(val label: String, val id: UUID)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LearningSystemSelect(
        modifier: Modifier = Modifier,
        items: List<SingleSelectItem>,
        onItemSelected: (UUID) -> Unit = {},
        selectedItem: SingleSelectItem?
) {
    var isExpanded = remember { mutableStateOf(false) }
    var textFieldSize = remember { mutableStateOf(Size.Zero) }

    val icon = if (isExpanded.value)
        Icons.Filled.KeyboardArrowUp
    else
        Icons.Filled.KeyboardArrowDown

    Column() {

        // Create an Outlined Text Field
        // with icon and not expanded
        OutlinedTextField(
                value = selectedItem?.label ?: "",
                onValueChange = { },
                modifier = Modifier
                        .fillMaxWidth()
                        .onGloballyPositioned { coordinates ->
                            // This value is used to assign to
                            // the DropDown the same width
                            textFieldSize.value = coordinates.size.toSize()
                        },
                label = { Text("Label") },
                trailingIcon = {
                    Icon(icon, "contentDescription",
                            Modifier.clickable { isExpanded.value = !isExpanded.value })
                },
                colors = TextFieldDefaults.outlinedTextFieldColors(
                        focusedBorderColor = Color(0xFF6650a4),
                        unfocusedBorderColor = Color(0xFF625b71),
                        cursorColor = Color.Transparent,
                        errorCursorColor = Color.Transparent)
        )

        // Create a drop-down menu with list of cities,
        // when clicked, set the Text Field text as the city selected
        DropdownMenu(
                expanded = isExpanded.value,
                onDismissRequest = { isExpanded.value = false },
                modifier = Modifier
                        .width(with(LocalDensity.current) { textFieldSize.value.width.toDp() }),
        ) {
            items.forEach { item ->
                DropdownMenuItem(
                        onClick = {
                            onItemSelected(item.id)
                            isExpanded.value = false
                        },
                        text= { Text(text = item.label, color= Color.Black) }
                )
            }
        }
    }
}