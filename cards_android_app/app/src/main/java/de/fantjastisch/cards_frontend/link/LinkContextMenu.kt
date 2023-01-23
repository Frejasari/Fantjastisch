package de.fantjastisch.cards_frontend.card

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.MoreVert
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.res.stringResource
import de.fantjastisch.cards.R
import de.fantjastisch.cards_frontend.card.update.UpdateCardFragment
import de.fantjastisch.cards_frontend.infrastructure.FantMainNavigator
import java.util.*


@Composable
fun LinkContextMenu(
    linkId: UUID,
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
                text = { Text(text = stringResource(R.string.card_menu_update)) },
                onClick = {
                    isMenuOpen.value = false
                   // navigator.push(UpdateCardFragment(cardId))
                })
            DropdownMenuItem(
                text = { Text(text = stringResource(id = R.string.card_menu_delete)) },
                onClick = {
                    isMenuOpen.value = false
                    onDeleteClicked()
                })
        }
    }
}