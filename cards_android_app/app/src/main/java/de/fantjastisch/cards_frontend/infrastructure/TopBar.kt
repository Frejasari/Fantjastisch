package de.fantjastisch.cards_frontend.infrastructure

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.outlined.AddCircle
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.res.stringResource
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.navigator.tab.TabNavigator
import de.fantjastisch.cards.R
import de.fantjastisch.cards_frontend.card.CreateCardFragment
import de.fantjastisch.cards_frontend.category.CreateCategoryFragment
import de.fantjastisch.cards_frontend.learning_object.CreateLearningObjectFragment
import de.fantjastisch.cards_frontend.learning_system.CreateLearningSystemFragment

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(
    navigator: Navigator,
    tabNavigator: TabNavigator,
) {
    // remember -> state nicht neu erzeugen, wenn Funktion neu aufgerufen wird.
    val isAddMenuOpen = remember { mutableStateOf(false) }
    val isContextMenuOpen = remember { mutableStateOf(false) }

    TopAppBar(
        colors = TopAppBarDefaults.smallTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.primary,
            titleContentColor = MaterialTheme.colorScheme.onPrimary,
            actionIconContentColor = MaterialTheme.colorScheme.onPrimary,
        ),
        title = { Text(text = tabNavigator.current.options.title) },
        actions = {
            IconButton(onClick = { isAddMenuOpen.value = !isAddMenuOpen.value }) {
                Icon(Icons.Outlined.AddCircle, contentDescription = "add")

                DropdownMenu(
                    expanded = isAddMenuOpen.value,
                    onDismissRequest = { isAddMenuOpen.value = false }
                ) {
                    DropdownMenuItem(
                        text = { Text(text = stringResource(R.string.menu_create_card)) },
                        onClick = {
                            isAddMenuOpen.value = false
                            navigator.push(CreateCardFragment())
                        })
                    DropdownMenuItem(
                        text = { Text(text = stringResource(id = R.string.menu_create_category)) },
                        onClick = {
                            isAddMenuOpen.value = false
                            navigator.push(CreateCategoryFragment())
                        })
                    DropdownMenuItem(
                        text = { Text("Neues Lernsystem") },
                        onClick = { isAddMenuOpen.value = false
                            navigator.push(CreateLearningSystemFragment()) })
                    DropdownMenuItem(
                        text = { Text("Neues Lernobjekt") },
                        onClick = {
                            isAddMenuOpen.value = false
                            navigator.push(CreateLearningObjectFragment())
                        })
                }
            }


            AnimatedVisibility(visible = tabNavigator.current == GlossaryTab) {
//                TODO
                IconButton(onClick = { isContextMenuOpen.value = !isContextMenuOpen.value }) {
                    Icon(Icons.Default.MoreVert, contentDescription = "more")

                    DropdownMenu(
                        expanded = isContextMenuOpen.value,
                        onDismissRequest = { isContextMenuOpen.value = false }
                    ) {}
                }

            }
        }
    )
}