package de.fantjastisch.cards_frontend.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AddCircle
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.res.stringResource
import cafe.adriel.voyager.navigator.Navigator
import de.fantjastisch.cards.R
import de.fantjastisch.cards_frontend.card.create.CreateCardFragment
import de.fantjastisch.cards_frontend.category.create.CreateCategoryFragment
import de.fantjastisch.cards_frontend.infrastructure.FantMainNavigator
import de.fantjastisch.cards_frontend.learning_object.create.CreateLearningObjectFragment
import de.fantjastisch.cards_frontend.learning_system.CreateLearningSystemFragment


/**
 * View für das Plus-Button Menü.
 *
 * @author Freja Sender, Semjon Nirmann, Tamari Bayer, Jessica Repty
 */
@Composable
fun TobBarCreateMenu() {
    val navigator: Navigator = FantMainNavigator.current
    val isCreateMenuOpen = remember { mutableStateOf(false) }

    IconButton(onClick = { isCreateMenuOpen.value = !isCreateMenuOpen.value }) {
        Icon(Icons.Outlined.AddCircle, contentDescription = "add")

        DropdownMenu(
            expanded = isCreateMenuOpen.value,
            onDismissRequest = { isCreateMenuOpen.value = false }
        ) {
            DropdownMenuItem(
                text = { Text(text = stringResource(R.string.menu_create_card)) },
                onClick = {
                    isCreateMenuOpen.value = false
                    navigator.push(CreateCardFragment())
                })
            DropdownMenuItem(
                text = { Text(text = stringResource(id = R.string.menu_create_category)) },
                onClick = {
                    isCreateMenuOpen.value = false
                    navigator.push(CreateCategoryFragment())
                })
            DropdownMenuItem(
                text = { Text(stringResource(id = R.string.menu_create_learningsystem)) },
                onClick = {
                    isCreateMenuOpen.value = false
                    navigator.push(CreateLearningSystemFragment())
                })
            DropdownMenuItem(
                text = { Text(text = stringResource(R.string.menu_create_learningobject)) },
                onClick = {
                    isCreateMenuOpen.value = false
                    navigator.push(CreateLearningObjectFragment())
                })
        }
    }
}