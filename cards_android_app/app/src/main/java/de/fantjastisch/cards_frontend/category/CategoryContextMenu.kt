package de.fantjastisch.cards_frontend.category


import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.MoreVert
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.res.stringResource
import cafe.adriel.voyager.navigator.Navigator
import de.fantjastisch.cards.R
import de.fantjastisch.cards_frontend.category.update.UpdateCategoryFragment
import de.fantjastisch.cards_frontend.infrastructure.FantMainNavigator
import java.util.*

/**
 * Zeigt das Kontext-Menü einer Kategorie.
 *
 * @param id Id, der ausgewählten Kategorie.
 * @param onDeleteClicked Funktion, mit der man eine Kategorie löschen kann.
 *
 * @author Tamari Bayer, Semjon Nirmann
 */
@Composable
fun CategoryContextMenu(
    id: UUID,
    onDeleteClicked: () -> Unit
) {
    val navigator = FantMainNavigator.current
    val isMenuOpen = remember { mutableStateOf(false) }


    IconButton(onClick = { isMenuOpen.value = !isMenuOpen.value }) {
        Icon(Icons.Outlined.MoreVert, contentDescription = "context actions")

        DropdownMenu(
            expanded = isMenuOpen.value,
            onDismissRequest = { isMenuOpen.value = false }
        ) {
            DropdownMenuItem(
                text = { Text(text = stringResource(id = R.string.update_button_text)) },
                onClick = {
                    isMenuOpen.value = false
                    navigator.push(UpdateCategoryFragment(id))
                })
            DropdownMenuItem(
                text = { Text(text = stringResource(id = R.string.delete_button_text)) },
                onClick = {
                    isMenuOpen.value = false
                    onDeleteClicked()
                })
        }
    }
}
