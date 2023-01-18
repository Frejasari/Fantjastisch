package de.fantjastisch.cards_frontend.category


import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.MoreVert
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.res.stringResource
import cafe.adriel.voyager.navigator.Navigator
import de.fantjastisch.cards.R
import java.util.UUID



    @Composable
    fun CategoryContextMenu(navigator: Navigator, id : UUID) {
        val isMenuOpen = remember { mutableStateOf(false) }

        IconButton(onClick = { isMenuOpen.value = !isMenuOpen.value }) {
            Icon(Icons.Outlined.MoreVert, contentDescription = "context actions")

            DropdownMenu(
                expanded = isMenuOpen.value,
                onDismissRequest = { isMenuOpen.value = false }
            ) {
                DropdownMenuItem(
                    text = { Text(text = stringResource(id = R.string.category_menu_update)) },
                    onClick = {
                        isMenuOpen.value = false
                        navigator.push(UpdateCategoryFragment(id))
                    })
                DropdownMenuItem(
                    text = { Text(text = stringResource(id = R.string.category_menu_delete)) },
                    onClick = {
                        isMenuOpen.value = false
                        //navigator.push(DeleteCategoryFragment(id))
                    })
            }
        }
    }