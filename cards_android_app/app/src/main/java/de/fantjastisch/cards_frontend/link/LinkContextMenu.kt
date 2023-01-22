 package de.fantjastisch.cards_frontend.link

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.MoreVert
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.res.stringResource
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import de.fantjastisch.cards.R
import de.fantjastisch.cards_frontend.infrastructure.FantMainNavigator
import de.fantjastisch.cards_frontend.link.update.UpdateLinkFragment
import java.util.UUID



@Composable
fun LinkContextMenu(
    linkId: UUID,
    cardId: UUID,
    name: String,
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
                    navigator.push(UpdateLinkFragment(linkId, cardId))
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
